import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class MyCrawler extends WebCrawler {
	
	Data myData;
	
	public MyCrawler() {
		myData = new Data();
	}

	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg"
			+ "|png|mp3|mp4|zip|gz))$");

	/**
	 * This method receives two parameters. The first parameter is the page
	 * in which we have discovered this new url and the second parameter is
	 * the new url. You should implement this function to specify whether
	 * the given url should be crawled or not (based on your crawling logic).
	 * In this example, we are instructing the crawler to ignore urls that
	 * have css, js, git, ... extensions and to only accept urls that start
	 * with "https://www.ics.uci.edu/". In this case, we didn't need the
	 * referringPage parameter to make the decision.
	 */
	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();
		return !FILTERS.matcher(href).matches()
				&& href.startsWith("http://djp3.westmont.edu/");
	}

	/**
	 * This function is called when a page is fetched and ready
	 * to be processed by your program.
	 */
	@Override
	public void visit(Page page) {
		String url = page.getWebURL().getURL();
		System.out.println("URL: " + url);

		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String text = htmlParseData.getText();
			Set<WebURL> links = htmlParseData.getOutgoingUrls();
			
			text = text.replaceAll("\\p{Punct}","");
			text = text.replaceAll("\\d","");
			
			myData.incLinks(links.size());
			myData.processText(url, text);
			myData.incPages();

			// We dump this crawler statistics after processing every 50 pages
	        if ((myData.getPages() % 5) == 0) {
	            System.out.println("Pages: " + myData.getPages());
	            System.out.println("Links: " + myData.getLinks());
	            System.out.println("Longest: " + myData.getLongestPage());
	            
	            Map<String, Integer> result = myData.getWordCount().entrySet()
	          		  .stream()
	          		  .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
	          		  .collect(Collectors.toMap(
	          		    Map.Entry::getKey, 
	          		    Map.Entry::getValue, 
	          		    (oldValue, newValue) -> oldValue, LinkedHashMap::new));
	          
	          int count = 0;
	          System.out.println("Top 25 words:");
	          for(String key : result.keySet()) {
	          	System.out.println(key + " (" + result.get(key) + ")");
	          	if(count == 24) {
	          		break;
	          	}
	          	count++;
	          }
	        }
		}
	}
	
	/**
     * Return the stored data of this crawler
     */
    @Override
    public Object getMyLocalData() {
        return myData;
    }
}
