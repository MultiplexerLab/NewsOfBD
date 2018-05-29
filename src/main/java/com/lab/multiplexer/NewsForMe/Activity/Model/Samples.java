package com.lab.multiplexer.NewsForMe.Activity.Model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Samples {
    @NonNull
    private static final List<Sample> audioSamples;
    @NonNull
    private static final List<Sample> videoSamples;

    static {

        //fm audioimage
        String audioImage = "http://files.softicons.com/download/application-icons/oropax-icon-set-by-878952/png/512/Broadcast.png";

        //fm channel
        String dhaka_fm ="http://118.179.219.244:8000/;stream.mp3";
        String abc_fm ="http://ample-zeno-01.radiojar.com/6a28tbx6vewtv?rj-ttl=5&rj-token=AAABW0mb8yADOr6mJqsZFWkCpsfwx56SJPnqI4G1MpaA9IfXSooiOg";
        String bangladesh_betar = "http://108.178.13.122:8081/;";
        String peoples_radio = "http://s3.myradiostream.com:14498/";
        String radio_today = "http://162.254.149.187:9302/stream/1/";
        String radio_2_fun = "http://radio.ethii.com:8000/airtime_128";
        String radio_goongoon = "http://media.dreamhousebd.com:8888/stream/1/";
        String lemon24bd = "http://office.mcc.com.bd:8000/";
        String bbc_bangla = "http://bbcwssc.ic.llnwd.net/stream/bbcwssc_mp1_ws-eieuk";
        String radio_moynamoti = "http://162.254.150.34:8207/;";
        String radio_shadhin = "http://89.163.128.81:8240/";
        String radio_sawdesh = "http://radioswadesh.com:8002/stream/1/";
        String gaan = "http://188.165.201.120:8016/stream";
        String bangla_wadio ="http://162.254.150.34:8201/stream/1/";
        String banglaoctor_e_radio ="http://184.154.43.106:8008/";
        String radio_songi = "http://162.254.150.34:7031/;";
        String radio_bangla_net = "http://sc6.spacialnet.com:33656/";
        String islamic_radio = "http://s7.voscast.com:7220/stream/1/";
        String radio_aamar = "http://202.126.122.43:9898/";
        String fnf_fm = "http://50.7.99.162:9555/stream/1/";
        String radio_city_hindi = "http://prclive1.listenon.in:9960/;";
        String radio_afsana = "http://174.36.206.197:8198/";
        String bollywood_radio = "http://96.31.83.86:8084/";
        String city_1016 = "http://18973.live.streamtheworld.com/ARNCITY_SC";
        String red = "http://13993.live.streamtheworld.com/CKYRFM_SC";
        String pura_dsi_fm ="http://puradsifm.net:9994/stream/1/";
        String one_fm = "http://sc-bb.1.fm:8017/;";
        String desi_music_mix = "http://66.23.234.242:8012/";
        String easy_fm = "http://ice8.securenetsystems.net/EASY96";

        //add fmm Stream to playList
        audioSamples = new LinkedList<>();

        audioSamples.add(new Sample("Gaan Fm ", gaan, audioImage));
        audioSamples.add(new Sample("Radio Sawdesh ", radio_sawdesh, audioImage));
        audioSamples.add(new Sample("Dhaka Fm", dhaka_fm, audioImage));
        audioSamples.add(new Sample("RED FM", red, audioImage));
        audioSamples.add(new Sample("ABC Radio", abc_fm, audioImage));
        audioSamples.add(new Sample("Betar Chittagong",bangladesh_betar , audioImage));
        audioSamples.add(new Sample("Peoples Radio",peoples_radio , audioImage));
        audioSamples.add(new Sample("Radio Today 89.6 FM", radio_today, audioImage));
        audioSamples.add(new Sample("Radio 2 Fun", radio_2_fun, audioImage));
        audioSamples.add(new Sample("Radio Goongoon",radio_goongoon , audioImage));
        audioSamples.add(new Sample("Lemon 24 BD", lemon24bd, audioImage));
        audioSamples.add(new Sample("BBC Bangla", bbc_bangla, audioImage));
        audioSamples.add(new Sample("Radio Moynamoti", radio_moynamoti , audioImage));
        audioSamples.add(new Sample("Radio Shadhin 92.4 FM", radio_shadhin , audioImage));
        audioSamples.add(new Sample("Banglawadio", bangla_wadio , audioImage));
        audioSamples.add(new Sample("One FM", one_fm , audioImage));
        audioSamples.add(new Sample("Desi Music Mix", desi_music_mix , audioImage));
        audioSamples.add(new Sample("Easy Fm", easy_fm , audioImage));
        audioSamples.add(new Sample("FnF FM", fnf_fm , audioImage));

        audioSamples.add(new Sample("City 1016", city_1016 , audioImage));
        audioSamples.add(new Sample("Islamic Radio ", islamic_radio , audioImage));
        audioSamples.add(new Sample("Puradsi FM", pura_dsi_fm , audioImage));
        audioSamples.add(new Sample("Radio Aamar ", radio_aamar , audioImage));
        audioSamples.add(new Sample("Radio Bangla Net ", radio_bangla_net , audioImage));
        audioSamples.add(new Sample("Radio City Hindi ", radio_city_hindi , audioImage));
        audioSamples.add(new Sample("Radio Shongi ", radio_songi , audioImage));
        audioSamples.add(new Sample("Bangla Doctor E-Radio", banglaoctor_e_radio , audioImage));
        audioSamples.add(new Sample("RadioAfsana - 24/7 Non-Stop", radio_afsana , audioImage));
        audioSamples.add(new Sample("Bollywood Radio", bollywood_radio , audioImage));


        //tv  channel
        String channel_9_hd="http://edge2.digijadoo.net/hls/channel_9_hi/index.m3u8";
        String channel_9_sd="http://edge2.digijadoo.net/hls/channel_9_mid/index.m3u8";
        String channel_9 = "http://edge2.digijadoo.net/hls/channel_9_low/index.m3u8";
        String zee_bangla = "http://edge3.digijadoo.net/hls/zee_bangla_hi/index.m3u8";
        String ekattor_tv = "http://edge3.digijadoo.net/hls/ekattur_tv_hi/index.m3u8";
        String jamuna_tv = "http://edge3.digijadoo.net/hls/jamuna_tv_hi/index.m3u8";
        String independent_tv = "http://edge3.digijadoo.net/hls/independent_tv_hi/index.m3u8";
        String somoy_tv = "http://edge3.digijadoo.net/hls/somoy_news_hi/index.m3u8";
        String bbc_world = "http://edge3.digijadoo.net/hls/bbc_world_hi/index.m3u8";
        String sa_tv = "http://edge3.digijadoo.net/hls/sa_tv_hi/index.m3u8";
        String channel_i = "http://edge3.digijadoo.net/hls/channel_i_hi/index.m3u8";
        String maasranga_tv = "http://edge3.digijadoo.net/hls/maasranga_tv_hi/index.m3u8";
        String atn_news = "http://edge3.digijadoo.net/hls/atn_news_hi/index.m3u8";
        String desh_tv = "http://edge3.digijadoo.net/hls/desh_tv_hi/index.m3u8";
        String boishakhi_tv = "http://edge3.digijadoo.net/hls/boishakhi_tv_hi/index.m3u8";
        String channel_24_tv = "http://edge3.digijadoo.net/hls/channel_24_hi/index.m3u8";
        String mohona_tv = "http://edge3.digijadoo.net/hls/mohona_tv_hi/index.m3u8";
        String zee_cinema = "http://edge3.digijadoo.net/hls/zee_cinema_hi/index.m3u8";
        String m_tv = "http://edge3.digijadoo.net/hls/mtv_hi/index.m3u8";
        String zee_tv ="http://edge3.digijadoo.net/hls/zee_tv_hi/index.m3u8";
        String bangla_vision = " http://edge3.digijadoo.net/hls/bangla_vision_hi/index.m3u8";
        String colors_bangla = "http://edge3.digijadoo.net/hls/colors_bangla_hi/index.m3u8";
        String color_asia = "http://edge3.digijadoo.net/hls/colors_asia_hi/index.m3u8";
        String abp_ananda = "http://edge3.digijadoo.net/hls/abp_ananda_hi/index.m3u8";
        String zee_clasic = "http://edge3.digijadoo.net/hls/zee_classic_hi/index.m3u8";
        String hum_sitarey = "http://edge3.digijadoo.net/hls/hum_sitarey_hi/index.m3u8";
        String ekushey_tv = "http://edge3.digijadoo.net/hls/ekushey_tv_hi/index.m3u8";
        String atn_bangla = "http://edge3.digijadoo.net/hls/atn_bangla_hi/index.m3u8";
        //  String show_time_usa = "http://edge-1112-us-la.filmon.com/live/1693.low.stream/playlist.m3u8?id=035bca1a71b11fce016d28acd3dbea51cbc96ddfe5b9ed3f8fe3286df6a738d37016c070b637806bf07f85eb57bb89f5595541bfd05466e724cb24ebcab92a91282b6ee4515c1fd21ae7221a61ca230c96eeb344ce6bececb4c1cdf9261d75fdac46108d0ca190e1796918c26263697d73c7d4f32d35a173aebfe3f6b0230a5feb8531dd8b6e74096fe1ae9aa0fd9f0f4ce84e857fa9e362";
        String p_tv_Sports = "http://edge3.digijadoo.net/hls/ptv_sports_hi/index.m3u8";
        String ten_one = "http://edge3.digijadoo.net/hls/ten_sports_1_hi/index.m3u8";
        String ten_two ="http://edge3.digijadoo.net/hls/ten_sports_2_hi/index.m3u8";
        String ten_cricket = "http://edge3.digijadoo.net/hls/ten_sports_hi/index.m3u8";
        String al_jaajira = "http://edge3.digijadoo.net/hls/al_jazeera_hi/index.m3u8";
        String times_now = "http://edge3.digijadoo.net/hls/times_now_hi/index.m3u8";
        String animall_planet = "http://edge3.digijadoo.net/hls/animal_planet_hi/index.m3u8";
        String nike = "http://edge3.digijadoo.net/hls/nickelodeon_hi/index.m3u8";
        String ntv = "http://edge3.digijadoo.net/hls/ntv_hi/index.m3u8";
        String g_tv = "http://edge3.digijadoo.net/hls/gazi_tv_hi/index.m3u8";
        //String channel_9_fhd="http://edge2.digijadoo.net/hls/channel_9_hi/index.m3u8";
        //Video items
        videoSamples = new ArrayList<>();
        videoSamples.add(new Sample("Gtv", g_tv));
        videoSamples.add(new Sample("Ntv", ntv));
        videoSamples.add(new Sample("Channel 9 ", channel_9));
        videoSamples.add(new Sample("Channel 9 SD ", channel_9_sd));
        //videoSamples.add(new Sample("Channel 9 HD ", channel_9_hd));
       // videoSamples.add(new Sample("Ptv Sports",p_tv_Sports ));
        //videoSamples.add(new Sample("Ten One",ten_one ));
        //videoSamples.add(new Sample("Ten Two",ten_two ));
        //videoSamples.add(new Sample("Ten Cricket", ten_cricket));
       // videoSamples.add(new Sample("AlJazeera", al_jaajira));
       // videoSamples.add(new Sample("Times Now",times_now ));
      //  videoSamples.add(new Sample("Animal Planet",animall_planet ));
       // videoSamples.add(new Sample("Nick",nike ));
        videoSamples.add(new Sample("Channel I",channel_i ));
        videoSamples.add(new Sample("Maasranga TV",maasranga_tv ));
        videoSamples.add(new Sample("Desh TV ",desh_tv ));
        videoSamples.add(new Sample("ATN Bangla ",atn_bangla));
        videoSamples.add(new Sample("Jamuna TV ", jamuna_tv));
        videoSamples.add(new Sample("Ekattor TV HD ",ekattor_tv ));
        videoSamples.add(new Sample("Indepandent TV ", independent_tv));
        videoSamples.add(new Sample("Somoy TV",somoy_tv));
        videoSamples.add(new Sample("ATN News Bangla", atn_news));
       // videoSamples.add(new Sample("Colors Bangla",colors_bangla ));
       // videoSamples.add(new Sample("Colors Asia ",color_asia ));
       // videoSamples.add(new Sample("ABP Ananda",abp_ananda ));
       // videoSamples.add(new Sample("Zee Clasic",zee_clasic ));
       // videoSamples.add(new Sample("Hum Sitarey",hum_sitarey ));
        videoSamples.add(new Sample("Boishakhi TV ",boishakhi_tv ));
        videoSamples.add(new Sample("Channel 24 ",channel_24_tv ));
        videoSamples.add(new Sample("Mohona TV",mohona_tv ));
        //videoSamples.add(new Sample("Zee Cinema", zee_cinema));
        //videoSamples.add(new Sample("M TV",m_tv ));
       // videoSamples.add(new Sample("Zee TV",zee_tv ));
        videoSamples.add(new Sample("Bangla Vision",bangla_vision ));
        videoSamples.add(new Sample("Ekushey TV",ekushey_tv ));
        videoSamples.add(new Sample("SA TV ", sa_tv));
       // videoSamples.add(new Sample("BBC World",bbc_world));
       // videoSamples.add(new Sample("Zee Bangla ", zee_bangla));
        // videoSamples.add(new Sample("Show Time USA ", show_time_usa));
         }

    @NonNull
    public static List<Sample> getAudioSamples() {
        return audioSamples;
    }

    @NonNull
    public static List<Sample> getVideoSamples() {
        return videoSamples;
    }

    /**
     * A container for the information associated with a
     * sample media item.
     */
    public static class Sample {
        @NonNull
        private String title;
        @NonNull
        private String mediaUrl;
        @Nullable
        private String artworkUrl;

        public Sample(@NonNull String title, @NonNull String mediaUrl) {
            this(title, mediaUrl, null);
        }

        public Sample(@NonNull String title, @NonNull String mediaUrl, @Nullable String artworkUrl) {
            this.title = title;
            this.mediaUrl = mediaUrl;
            this.artworkUrl = artworkUrl;
        }

        @NonNull
        public String getTitle() {
            return title;
        }

        @NonNull
        public String getMediaUrl() {
            return mediaUrl;
        }

        @Nullable
        public String getArtworkUrl() {
            return artworkUrl;
        }
    }
}
