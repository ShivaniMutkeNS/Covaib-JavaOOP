public class WordExporter implements DocumentExporter {
	@Override
	public String export(String content) {
		return "<word>" + content + "</word>";
	}
}


