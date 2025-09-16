public class HtmlExporter implements DocumentExporter {
	@Override
	public String export(String content) {
		return "<html><body>" + content + "</body></html>";
	}
}


