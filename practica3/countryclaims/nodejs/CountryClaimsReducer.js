(function(process, logger){
	'use strict';
	const readline = require('readline');

	const rl = readline.createInterface({
		input: process.stdin,
		output: process.stdout,
		terminal: false
	});

	var previousWord = false,
		current_count = 0,
		current_value = 0;

	function reduceFn(line){

		let parts = line.split("\t");
		let count = parseInt(parts[1]);
		let word = parts[0].trim();

		if (previousWord === false ){
			current_value = count;
			current_count = 1;
			previousWord = word;
		}else if (word !== previousWord){
			logger.log(previousWord + "\t" + Math.floor(current_value / current_count) );
			current_value = count;
			current_count = 1;
			previousWord = word;
		} else {
			current_value += count;
			current_count++;
		}
	}

	function flush(){
		logger.log(previousWord + "\t" + Math.floor(current_value / current_count) );
	}

	rl.on('line', reduceFn);
	rl.on('close', flush);

})(process, console);

