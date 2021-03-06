package feature_extraction;

import java.io.File;
import java.util.Map;

public class linguistic_miner_of_text {
	File liwcCatFile;
	LIWCDictionaryHelper liwcDic;
	
	//Provide LIWC CAT file where all dictionary of words are present
	public linguistic_miner_of_text(String LIWC_dictionary_file_location) {
		liwcCatFile = new File(LIWC_dictionary_file_location);
		liwcDic = new LIWCDictionaryHelper(liwcCatFile);
	}
	
	//Given Text it returns value to be returned
	public Map<String, Double> get_liwc_value_of_text(String text) {
		Map<String, Double> dictnioary_of_liwc_value_for_given_text;
		dictnioary_of_liwc_value_for_given_text = liwcDic.getCounts(text);
		return dictnioary_of_liwc_value_for_given_text;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//linguistic_miner_of_text l1 = new linguistic_miner_of_text("settings/LIWC.CAT");
		//String text = "Well, right now I just woke up from a mid-day nap. It's sort of weird, but ever since I moved to Texas, I have had problems concentrating on things. I remember starting my homework in  10th grade as soon as the clock struck 4 and not stopping until it was done. Of course it was easier, but I still did it. But when I moved here, the homework got a little more challenging and there was a lot more busy work, and so I decided not to spend hours doing it, and just getting by. But the thing was that I always paid attention in class and just plain out knew the stuff, and now that I look back, if I had really worked hard and stayed on track the last two years without getting  lazy, I would have been a genius, but hey, that's all good. It's too late to correct the past, but I don't really know how to stay focused n the future. The one thing I know is that when  people say that b/c they live on campus they can't concentrate, it's b. s. For me it would be easier there, but alas, I'm living at home under the watchful eye of my parents and a little nagging sister that just nags and nags and nags. You get my point.";
		//System.out.println(l1.get_liwc_value_of_text(text));

	}

}
