package es.loksly;

import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.util.Progressable;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;


import org.apache.hadoop.io.IOUtils;

public class FileSystemSplit {
	public static void main( String [ ] args )
		throws Exception {

			ArgumentParser parser = ArgumentParsers
				.newArgumentParser("FileSystemSplit")
				.defaultHelp(true)
				.version("0.1")
				.description("Copy and trim files between filesystems.");
			parser
				.addArgument("-i", "--in")
				.required(true)
				.help("Specify origin file");
			parser
				.addArgument("-o", "--out")
				.required(true)
				.help("Specify destination file");
			parser
				.addArgument("-f")
				.required(false)
				.help("Specify what happens if the file exists. If the parameter is present then the file should be replaced.");
			parser
				.addArgument("--progress")
				.required(false)
				.help("Show progress bar.");
			parser
				.addArgument("-p", "--percentage")
				.setDefault("100")
				.type(Integer.class)
				.help("Specify percentage. Value should be an integer between [0-100].");
			parser
				.addArgument("-s", "--start")
				.setDefault("0")
				.type(Integer.class)
				.help("Specify start percentage. Value should be an integer between [0-100]. If percentage argument plus start is greater than 100 then the file will be copied to the end.");
/*
			parser
				.addArgument("-b", "--buffer")
				.setDefault("4096")
				.type(Integer.class)
				.help("Specify buffer size. Value should be an integer between [1024-4096].");
*/
			Namespace ns = null;
			try {
				ns = parser.parseArgs(args);
			} catch (ArgumentParserException e) {
				parser.handleError(e);
				parser.printHelp();
				System.exit(1);
			}

			Configuration conf = new Configuration();

			String originstr = ns.getString("in");
			String deststr = ns.getString("out");
			boolean force = (ns.get("f") == null);
			boolean progress = (ns.get("progress") == null);

			if (originstr == null || deststr == null){
				System.err.println("Both source and destination files must be specified. Check the --in and the --out parameter.");
				System.exit(2);
			}

			FSDataInputStream in = null ;
			FSDataOutputStream out = null ;
			try {
				URI originuri = URI.create(originstr);
				URI desturi = URI.create(deststr);

				FileSystem originfs = FileSystem.get( originuri, conf );
				FileSystem destfs = FileSystem.get( desturi, conf );
				
				Boolean sourceexists = originfs.exists(new Path(originstr));
				Boolean destinationexists = destfs.exists(new Path(deststr));
				
				if (!sourceexists){
					System.err.println("Source file must exist. Check --in parameter.");
					System.exit(3);
				}

				if (destinationexists && !force){
					System.err.println("Destination file exist use the force\u2122 parameter to replace.");
					System.exit(4);
				}

				FileStatus source = originfs.getFileStatus(new Path(originstr));

				if (source.isDirectory()) {
					System.err.println("Source must be a file. Check --in parameter.");
					System.exit(5);
				}

				int percentage = ns.getInt("percentage");
				int start = ns.getInt("start");
/*				int buffer = ns.getInt('buffer'); */

				if (start < 0){
					System.err.println("Bad start percentage. Check --start parameter.");
					System.exit(6);
				}
				if (percentage < 0){
					System.err.println("Bad percentage. Check --percentage parameter.");
					System.exit(7);
				}
				if (start > 100){
					start = 100;
				}
				if (percentage > 100){
					percentage = 100;
				}
				int until = start + percentage;
				if (until > 100){
					until = 100;
				}

				long length = source.getLen();
				long startbyte = length * start / 100;
				long endbyte = length * until / 100;

				if (progress){
					out = destfs.create(new Path(deststr), new Progressable() {
						@Override
						public void progress() {
							System.out.println(".");
						}
					});
				}else {
					out = destfs.create(new Path(deststr));
				}
				in = originfs.open(new Path( originuri) );

				FSDataInputStream fis = new FSDataInputStream(in);
				fis.seek(startbyte);
				
				//@loksly: this is one example of how Java method overloading can be bad used.
				//check: https://hadoop.apache.org/docs/r2.6.1/api/index.html?org/apache/hadoop/io/IOUtils.html
				//you can see that if the third parameter is a long then it means the number of bytes to copy instead of buffer size.
				IOUtils.copyBytes( fis, out, endbyte - startbyte, true );

			}catch(Exception e){
				System.err.println(e);
				System.exit(100);
			} finally {
				if (in != null){
					IOUtils.closeStream( in );
					IOUtils.closeStream( out );
				}
			}
	}
}