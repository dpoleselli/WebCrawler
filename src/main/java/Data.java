import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Data {
	private int pages;
    private long links;
    private int longestPageLength;
    private String longestPage;
    private Map<String, Integer> wordCount = new HashMap<>();
    private String[] STOP = new String[] {

"a",
"about",
"above",
"after",
"again",
"against",
"all",
"am",
"an",
"and",
"any",
"are",
"aren't",
"as",
"at",
"be",
"because",
"been",
"before",
"being",
"below",
"between",
"both",
"but",
"by",
"can't",
"cannot",
"could",
"couldn't",
"did",
"didn't",
"do",
"does",
"doesn't",
"doing",
"don't",
"down",
"during",
"each",
"few",
"for",
"from",
"further",
"had",
"hadn't",
"has",
"hasn't",
"have",
"haven't",
"having",
"he",
"he'd",
"he'll",
"he's",
"her",
"here",
"here's",
"hers",
"herself",
"him",
"himself",
"his",
"how",
"how's",
"i",
"i'd",
"i'll",
"i'm",
"i've",
"if",
"in",
"into",
"is",
"isn't",
"it",
"it's",
"its",
"itself",
"let's",
"me",
"more",
"most",
"mustn't",
"my",
"myself",
"no",
"nor",
"not",
"of",
"off",
"on",
"once",
"only",
"or",
"other",
"ought",
"our",
"ours",
"ourselves",
"out",
"over",
"own",
"same",
"shan't",
"she",
"she'd",
"she'll",
"she's",
"should",
"shouldn't",
"so",
"some",
"such",
"than",
"that",
"that's",
"the",
"their",
"theirs",
"them",
"themselves",
"then",
"there",
"there's",
"these",
"they",
"they'd",
"they'll",
"they're",
"they've",
"this",
"those",
"through",
"to",
"too",
"under",
"until",
"up",
"very",
"was",
"wasn't",
"we",
"we'd",
"we'll",
"we're",
"we've",
"were",
"weren't",
"what",
"what's",
"when",
"when's",
"where",
"where's",
"which",
"while",
"who",
"who's",
"whom",
"why",
"why's",
"with",
"won't",
"would",
"wouldn't",
"you",
"you'd",
"you'll",
"you're",
"you've",
"your",
"yours",
"yourself",
"yourselves"
   };
    
    private Set<String> STOPWORDS = new HashSet<String>(Arrays.asList(STOP));
    
    //return the longest page with its length
    public String getLongestPage() {
    	return longestPage + ':' + longestPageLength;
    }
    
    //check if a new page is the longest and update values in necessary
    public void checkLongestPage(String page, int length) {
    	if(length > longestPageLength) {
    		this.longestPageLength = length;
    		this.longestPage = page;
    	}
    }
    
    //check the length and handle word processing
    public void processText(String url, String text) {
    	if(text == null || text.isEmpty()) {
    		return;
    	}
    	
    	String[] words = text.split("\\s+");
		checkLongestPage(url, words.length);
    	
		//loop through each word in the page
		for(String word : words) {
			word = word.toLowerCase();
    		if(!STOPWORDS.contains(word)) {
    			//count words
    			if(wordCount.containsKey(word)) {
    				wordCount.put(word, wordCount.get(word) + 1);
    			}
    			else {
    				wordCount.put(word, 1);
    			}
    		}
    	}
    }
    
    //get the number of processed pages
    public int getPages() {
        return pages;
    }

    //set the number of processed pages
    public void setPages(int pages) {
        this.pages = pages;
    }

    //increment the number of processed pages by one
    public void incPages() {
        this.pages++;
    }

    //get the number of links
    public long getLinks() {
        return links;
    }

    //set the number of links
    public void setLinks(long links) {
        this.links = links;
    }

    //increment the number of links by some number
    public void incLinks(int count) {
        this.links += count;
    }
}
