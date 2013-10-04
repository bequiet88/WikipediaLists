package de.unimannheim.dws.wikilist.reader;

public interface IListPageReader<T> {

	public void openInput(ReaderResource resource);

	public T readInput() throws Exception;

	public void close();

	public void writeOutputToFile(String path, String text);

}
