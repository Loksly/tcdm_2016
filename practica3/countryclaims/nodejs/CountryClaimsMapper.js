(function(process, logger){
	'use strict';
	const readline = require('readline');

	const rl = readline.createInterface({
		input: process.stdin,
		output: process.stdout,
		terminal: false
	});

	let firstline = true;
	function splitAndChoose(line){
		if (firstline){
			firstline = false;
			return;
		}
		let parts = line.split(",");
		if (parts[8] === ''){
			parts[8] = '0';
		}
		logger.log(parts[4].replace("\"", "").replace("\"", "") + "\t" + parts[8]);
	}

	rl.on('line', splitAndChoose);

})(process, console);
