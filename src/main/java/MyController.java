import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class MyController {
	public static void main(String[] args) throws Exception {
		long time = System.currentTimeMillis();
		String crawlStorageFolder = "data/crawl/root";
		int numberOfCrawlers = 3;

		CrawlConfig config = new CrawlConfig();
		config.setCrawlStorageFolder(crawlStorageFolder);
		config.setPolitenessDelay(100);
		config.setUserAgentString(args[0]);

		// Instantiate the controller for this crawl.
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

		// For each crawl, you need to add some seed urls. These are the first
		// URLs that are fetched and then the crawler starts following links
		// which are found in these pages
		controller.addSeed(args[1]);


		// The factory which creates instances of crawlers.
		CrawlController.WebCrawlerFactory<MyCrawler> factory = MyCrawler::new;

		// Start the crawl. This is a blocking operation, meaning that your code
		// will reach the line after this only when crawling is finished.
		controller.start(factory, numberOfCrawlers);

		List<Object> crawlersLocalData = controller.getCrawlersLocalData();
		long totalLinks = 0;
		int totalProcessedPages = 0;
		String longestPage = "";
		int longestPageLength = 0;
		Map<String, Integer> wordCount = new HashMap<>();
		Set<String> banksoopy = new HashSet<>();
		Map<String, Integer> gram = new HashMap<>();

		//loop through the data of each crawler
		for (Object localData : crawlersLocalData) {
			Data stat = (Data) localData;
			totalLinks += stat.getLinks();
			totalProcessedPages += stat.getPages();

			String longest = stat.getLongestPage();
			if(longest != null && !longest.isEmpty()) {
				String[] arr = longest.split(":;:");
				//check if there is a new longest page
				if(Integer.parseInt(arr[1]) > longestPageLength) {
					longestPageLength = Integer.parseInt(arr[1]);
					longestPage = arr[0];
				}
			}

			//determine word counts
			Map<String, Integer> words = stat.getWordCount();
			for(String word : words.keySet()) {
				if(wordCount.containsKey(word)) {
					wordCount.put(word, wordCount.get(word) + words.get(word));
				}
				else {
					wordCount.put(word, words.get(word));
				}
			}

			Map<String, Integer> gramCount = stat.getGram();
			//loop through pairs of words
			for(String phrase : gramCount.keySet()) {
				if(gram.containsKey(phrase)) {
					gram.put(phrase, gram.get(phrase) + gramCount.get(phrase));
				}
				else {
					gram.put(phrase, gramCount.get(phrase));
				}
			}

			banksoopy.addAll(stat.getBanksoopy());
		}


		//sorting algorithm from https://www.baeldung.com/java-hashmap-sort
		Map<String, Integer> gramFrequency = gram.entrySet()
				.stream()
				.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				.collect(Collectors.toMap(
						Map.Entry::getKey, 
						Map.Entry::getValue, 
						(oldValue, newValue) -> oldValue, LinkedHashMap::new));

		//sorting algorithm from https://www.baeldung.com/java-hashmap-sort
		Map<String, Integer> result = wordCount.entrySet()
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

		count = 0;
		System.out.println("Top 25 2 grams:");
		for(String key : gramFrequency.keySet()) {
			System.out.println(key + " (" + gramFrequency.get(key) + ")");
			if(count == 24) {
				break;
			}
			count++;
		}

		System.out.println("Total time(sec): " + (System.currentTimeMillis() - time)/1000);
		System.out.println("Total links: " + totalLinks);
		System.out.println("Total pages: " + totalProcessedPages);
		System.out.println("Longest page: " + longestPage + " (" + longestPageLength + ")");
		System.out.println("Pages with Banksoopy Brickle: ");
		for(String url : banksoopy) {
			System.out.println("    " + url);
		}
	}
}
