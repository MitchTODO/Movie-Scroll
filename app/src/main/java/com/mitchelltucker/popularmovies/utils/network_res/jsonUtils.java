package com.mitchelltucker.popularmovies.utils.network_res;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mitchelltucker.popularmovies.utils.encode_objects.Movie;
import com.mitchelltucker.popularmovies.utils.encode_objects.Movies;
import com.mitchelltucker.popularmovies.utils.encode_objects.Review;
import com.mitchelltucker.popularmovies.utils.encode_objects.Reviews;
import com.mitchelltucker.popularmovies.utils.encode_objects.Video;
import com.mitchelltucker.popularmovies.utils.encode_objects.Videos;


public class jsonUtils {
    // debug tag
    private static final String TAG = "JSON";

    /**
     *  JsonArrayToArrayList
     * @param array : JSONArray
     * @return List<Integer>
     */
    private static List<Integer> JsonArrayToArrayList(JSONArray array){
        int len = array.length();
        List<Integer> newArray = new ArrayList<>();
        for (int i=0;i<len;i++){
            try {
                newArray.add(array.getInt(i));
            }catch (Exception e) {
                Log.e(TAG,String.valueOf(e));
            }
        }
        return newArray;
    }

    /**
     * parseJsonData
     * @param json : String
     * @return Movies
     */
    public static Movies parseJsonData(String json) {
        int page;
        int total_results;
        int total_pages;
        List<Movie> results = new ArrayList<>();
        try {
            JSONObject movieObject = new JSONObject(json);
            page = movieObject.getInt("page");
            total_results = movieObject.getInt("total_results");
            total_pages = movieObject.getInt("total_pages");
            JSONArray movieContext = movieObject.getJSONArray("results");
            for(int i = 0; i < movieContext.length(); i++){
                JSONObject m = movieContext.getJSONObject(i);
                int popularity = m.getInt("popularity");
                int vote_count = m.getInt("vote_count");
                String poster_path = m.getString("poster_path");
                int id = m.getInt("id");
                boolean adult = m.getBoolean("adult");
                String backdrop_path = m.getString("backdrop_path");
                String original_language = m.getString("original_language");
                String original_title = m.getString("original_title");
                JSONArray genre_ids = m.getJSONArray("genre_ids");
                String title = m.getString("title");
                float vote_average = Float.parseFloat(m.getString("vote_average"));
                String overview = m.getString("overview");
                String release_date = m.getString("release_date");
                List<Integer> fixedArray = JsonArrayToArrayList(genre_ids);

                Movie movieInfo = new Movie();
                movieInfo.setPopularity(popularity);
                movieInfo.setVote_count(vote_count);
                movieInfo.setVideo(false);
                movieInfo.setPoster_path(poster_path);
                movieInfo.setId(id);
                movieInfo.setAdult(adult);
                movieInfo.setBackdrop_path(backdrop_path);
                movieInfo.setOriginal_language(original_language);
                movieInfo.setOriginal_title(original_title);
                movieInfo.setGenre_ids(fixedArray);
                movieInfo.setTitle(title);
                movieInfo.setVote_average(vote_average);
                movieInfo.setOverview(overview);
                movieInfo.setRelease_date(release_date);
                results.add(movieInfo);
            }

        }catch(Exception e ){
            Log.e(TAG,"Error "+ e.getMessage());
            return null;
        }

        Movies movies = new Movies();
        movies.setpage(page);
        movies.setTotal_pages(total_pages);
        movies.setTotal_results(total_results);
        movies.setResults(results);
        return movies;
    }

    /**
     * videoParse
     * @param json : Sting
     * @return Videos
     */
    public static Videos videoParse(String json){
        int id;
        List<Video> results = new ArrayList<>();
        try {
            JSONObject videoObject = new JSONObject(json);
            id = videoObject.getInt("id");

            JSONArray videoContext = videoObject.getJSONArray("results");
            for(int i = 0; i < videoContext.length(); i++) {
                JSONObject v = videoContext.getJSONObject(i);
                String v_id = v.getString("id");
                String v_laugauge = v.getString("iso_639_1");
                String v_region = v.getString("iso_3166_1");
                String v_key = v.getString("key");
                String v_name = v.getString("name");
                String v_site = v.getString("site");
                int v_size = v.getInt("size");
                String v_type = v.getString("type");

                Video newVideo = new Video();
                newVideo.setId(v_id);
                newVideo.setLanguage(v_laugauge);
                newVideo.setRegion(v_region);
                newVideo.setKey(v_key);
                newVideo.setName(v_name);
                newVideo.setSite(v_site);
                newVideo.setSize(v_size);
                newVideo.setType(v_type);
                results.add(newVideo);
            }

        }catch(Exception e ){
            Log.e(TAG,"Error "+ e.getMessage());
            return null;
        }
        Videos videos = new Videos();
        videos.setId(id);
        videos.setResults(results);

        return videos;
    }

    /**
     * reviewParse
     * @param json : String
     * @return Reviews
     */
    public static Reviews reviewParse(String json){
        int id;
        List<Review> results = new ArrayList<>();
        try {
            JSONObject videoObject = new JSONObject(json);
            id = videoObject.getInt("id");

            JSONArray videoContext = videoObject.getJSONArray("results");
            for(int i = 0; i < videoContext.length(); i++) {
                JSONObject v = videoContext.getJSONObject(i);
                String v_author = v.getString("author");
                String v_content = v.getString("content");
                String v_id = v.getString("id");
                String v_url = v.getString("url");

                Review newReview = new Review();
                newReview.setAuthor(v_author);
                newReview.setContent(v_content);
                newReview.setId(v_id);
                newReview.setUrl(v_url);
                results.add(newReview);
            }
        }catch(Exception e ){
            Log.e(TAG,"Error "+ e.getMessage());
            return null;
        }
        Reviews reviews = new Reviews();
        reviews.setId(id);
        reviews.setResults(results);
        return reviews;
    }
/*
    // JSON OBJECT EXAMPLE FROM THEMOVIEDB.org
    {
        "id": 420818,
            "page": 1,
            "results": [
        {
            "author": "SWITCH.",
                "content": "‘The Lion King’ is a catastrophe; a new low in the ever-diminishing returns of Disney’s endless run of remakes. There’s nothing redeeming about it, with every decision either ill-conceived or mishandled to the point of incompetence. In Favreau’s hands, ‘The Lion King’ is rendered thunderously dull, lacking in any tension or complex characterisation, taking a laboriously long time to go nowhere and never once justifying its contentious existence. Even with my dislike of the original, I was flabbergasted at how thoroughly this film never attempts to understand why so many people love the 1994 film. If nothing else, this film makes it abundantly clear that Disney has no interest in making great cinema or honouring its own legacy. They don’t care whether the film is good or whether you enjoy it. All they care about is using nostalgia to trick you into buying your ticket so they can make as much money off you as they can, and maybe if they throw some recognisable iconic moments from your childhood on the screen, they may even be able to fool you into thinking you’d had a good time. ‘The Lion King’ is the ultimate diabolical apex of the commercialisation of nostalgia, and its inevitable box office success will just prove how easily we continue to be duped and how thoroughly they have trained us to not care about the quality of what we see. If this really is the future of mainstream cinema, then we are in serious, serious trouble.\r\n- Daniel Lammin\r\n\r\nRead Daniel's full article...\r\nhttps://www.maketheswitch.com.au/article/review-the-lion-king-a-catastrophic-and-soulless-remake-of-a-disney-classic",
                "id": "5d2ddc7d6a300b0011a469df",
                "url": "https://www.themoviedb.org/review/5d2ddc7d6a300b0011a469df"
        },
        {
            "author": "msbreviews",
                "content": "If you enjoy reading my Spoiler-Free reviews, please follow my blog :)\r\n\r\nI don’t know how I should start, but I guess I’ll address something that people might ask: yes, it’s a SPOILER-FREE review. Why? Well, the story might follow the same essential plot points, and the characters might have similar narrative paths, but there are so many details that make this movie stand on its own. From tiny little improvements to moments of the original that wouldn’t make sense in a realistic environment to adjustments to character’s backstory, musical moments or other significant parts. With that said, I need to discuss the controversy surrounding this remake, but I won’t take longer than one paragraph.\r\n\r\nPeople need to understand that these Disney’s remakes aren’t here to replace the originals. They’re here to honor them, and bring their stories and characters to this new century so that new generations can have an additional look at something they love, and 90s kids can remember why they love these films so much. Emphasis on the “additional” part of that sentence. Then, people also need to get their preferences right: would you want to watch a shot-for-shot remake or something entirely different? Or a blend of these two? If you don’t know what you want, you might be in danger of turning into a hypocrite if your speech goes from “I don’t want these copy-paste remakes” to “they changed that specific moment, why didn’t they keep it the same?” Just be clear on what you wish. If you simply don’t want Disney to do these remakes, then just don’t watch them. Don’t go online try to beat it to the ground with negative comments if you haven’t seen the movie. Moving on …\r\n\r\nI love it. I absolutely love it. I cried the exact same 4 times as I did in the 1994’s original. My whole body got chills during the opening sequence, which is one of a few things I love more about the remake than in the original. It’s NOT a shot-for-shot remake! I don’t understand how so many critics are calling it so. Either people’s memory of the original faded or someone clearly didn’t watch the same film. I can write a whole review of 1000+ words just describing the new stuff. Of course, the story goes through the same iconic moments in the same way, and some dialogues are extensively repeated, which was something that I was hoping they developed more. Despite that, I still feel that the scripts have a different take on it from the voice actors, even James Earl Jones.\r\n\r\nOne proof would be that I cried on a scene that I never felt like it in the original. I don’t know if it was how it was shot (one of the various different angles that the remake provides of known scenes) or if the dialogue just has more impact this time around, but the point here is that this remake is NOT a cheap copy-paste. I always look forward to seeing what they come up with to solve or adjust some questions that the originals leave us with. Let me just write that Jeff Nathanson has some truly brilliant changes/additions. Remember people criticizing Scar’s look when the first teaser came out? With just ONE WORD, its entire physical shape, scar, and past are explained. One word. Imagine that. There are little elements like adding a word or a sentence here and there, and it makes so much more sense with the character or the story in question.\r\n\r\nSpeaking of Scar, Chiwetel Ejiofor is astonishing. Scar might be my favorite character of the remake. He’s more menacing and scarier, his voice is darker, and his arc is better explored. Jeremy Irons will always have that iconic voice associated with the character, but Ejiofor did a crazily good job in replacing him. However, if there’s one voice that I could never watch another actor do is Mufasa’s. I have no words to express how emotionally powerful James Earl Jones’ voice is. As soon as he says “Simba” in the reflection scene, my eyes drop waterfalls. His voice is an emotional trigger, let’s call it that. I love Timon (Billy Eichner) and Pumbaa (Seth Rogen) even more this time around. Their scenes are hilarious, and the characters’ relationship keeps being a standout. Oh, and if you were worried that the hula scene would never be as good let alone surpassed … You might need to rethink that. Also, I enjoyed the stretched last act (I found the original’s final battle too abrupt), and I would advise parents to be careful showing this remake to (very) young kids since the violence on display feels much more real (duh).\r\n\r\nJD McCrary and Shahadi Wright-Joseph (young Nala) are amazing, and their voices are crystal clear while singing. Donald Glover and Beyoncé (adult Nala) are also terrific, and their voices are even better. The new music Spirit fits better in this remake than Speechless in Aladdin. Moving on to the music, it’s another aspect that I genuinely think the remake does better. Hans Zimmer proves that he can bring an old score of his back to life in a much more robust, epic, and passionate way. Every song feels more prominent, every soundtrack feels a lot more impactful and stronger. Be Prepared is the only one that goes through a significant change, and while it might sound a bit strange at first, I love it more each time I listen to it. It’s a score that will never be forgotten, and this remake just helped people remember how great it is. Oscar-winning score.\r\n\r\nI left the best to last: the visuals. I can’t possibly describe how impressive and eyegasmic the CGI is. Animals talking was never an issue (people keep sharing GIFs or short clips and immediately started complaining that it looked awkward … a 30-seconds video without context watched on a laptop will never give you a hint of how the movie will actually be). Yes, the expressiveness of the original animation can’t be achieved, but going as far as saying that the film lacks soul or that the characters don’t emote is just inaccurate. You don’t need a PhD to understand that a lion with its ears down or up means different things. There are tiny little movements in the animals that are so complex that I’m still astounded how they were able to do it. If a bug flies near their faces, they flinch or move in such a characteristic way that I felt like I was truly watching real animals. Once again, Oscar-winning VFX.\r\n\r\nI don’t really have major problems with it. Minor gripes with a few things, but the biggest one would be the lack of more uniqueness. There’s no element of surprise in regards to the story or the character’s decisions. We always know what’s coming, so we’re prepared (no pun intended) for anything they through at us because, well, we’ve seen it before (with the exception of one particular scene that made me jump out of my chair and I think not a single person will be able to avoid it). I find the “animals don’t emote” argument one of the biggest nitpicks in the history of cinema. It might be true that they lack the emotion of the original animation, but going as far as saying that they show absolutely no emotion is just hating for the sake of hate. Same goes for people criticizing the fact that Can You Feel the Love Tonight is sung during broad daylight … In the original, it isn’t nighttime as well.\r\n\r\nFinally, I just want to address the “these remakes aren’t necessary / no one asked for these” discussion. No one asked for the 1994’s movie until it came out. People didn’t know they needed it. Seriously, everyone needs to realize that these remakes aren’t here to replace the originals. How many of you have watched The Lion King (1994) or showed it to someone in the past 10 years? How many times have you heard its score in the same period? I bet that most of the answers are simply “none”. That’s how important this remake is then! It makes you go back, it makes everyone remember how incredible the 1994’s film is, by keeping its essence while being able to stand on its own. Jon Favreau did a tremendous job, and I hope he gets recognized for it.\r\n\r\nIn the end, it doesn’t matter if the story is identical if we cry all the same. It doesn’t matter if we know what’s coming if we still feel nervous and worried about the characters. The Lion King (2019) is one of Disney’s best remakes so far, on par with The Jungle Book. Its VFX are game-changing, its score is more powerful and emotional than in the original, and the story carries the same heartfelt impact. James Earl Jones’ voice is everything. Timon and Pumbaa are even funnier. Ejiofor’s Scar is the best character in this remake. I have no flaws to point out, except that it follows the exact same path that the original’s story does. I wish it would be more distinct, but I can’t lie to myself, I love it deeply. One of 2019’s best movies. My #1 spot will be hard to decide… Go watch it! I can’t wait to see it again!\r\n\r\nRating: A",
                "id": "5d31d3991d78f2000ee0b492",
                "url": "https://www.themoviedb.org/review/5d31d3991d78f2000ee0b492"
        }
  ],
        "total_pages": 1,
            "total_results": 2
    }
*/
}
