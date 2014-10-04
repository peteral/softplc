function main() {
	memory.write("M,W100", memory.read("M,W100").intValue() + 1);
	
	logger.info("New value: " + memory.read("M,W100"));
}
