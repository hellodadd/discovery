package com.freeme.discovery.http;

import java.util.List;

/**
 * Created by server on 16-11-2.
 */

public class TestShopApi {
    /**
     * code : 200
     * msg : success
     * newslist : [{"ctime":"2016-11-01","title":"衣服颜色透露了你的性格，which one？","description":"好工坊","picUrl":"http://mmbiz.qpic.cn/mmbiz/ImBRhLUmkb3slXBmKvcnfhU51TlGq3beKY6tFZoWQicTnSDBfiaicic7T7rJsHzplIXGsSJahTbqkOhJfdgCymicibjg/0?wx_fmt=jpeg","url":"http://mp.weixin.qq.com/s?__biz=MzAwOTUxMzIwOA==&mid=400681065&idx=2&sn=627cf3e25d149c088d50b7d4415afb7b#rd"},{"ctime":"2016-11-01","title":"【撩妹魔术】把手机当着妹子面塞进吹起的气球","description":"火热内涵视频","picUrl":"http://mmbiz.qpic.cn/mmbiz_png/5mtyy5fKP9bCXFHqbAibT2ngHDPJz7F3USJ4UK3myLwP5YcpcPTKNZ7Y9jyfibpMULYXI60181h10ur1nmAiafKeQ/0?wx_fmt=png","url":"http://mp.weixin.qq.com/s?__biz=MzIxOTYxOTQ4Ng==&mid=2247483780&idx=3&sn=dc71d2cda1430aa0efe8831405bc504e&chksm=97d9c875a0ae4163738e24bae2360cd2be174d"},{"ctime":"2016-11-01","title":"【撩妹魔术】白开水变果汁","description":"火热内涵视频","picUrl":"http://mmbiz.qpic.cn/mmbiz_png/5mtyy5fKP9bCXFHqbAibT2ngHDPJz7F3URTVvjuQxERjN23cKibicm60pbNMepj9kLnEJY0xd1cvZTnRheZyObPIg/0?wx_fmt=png","url":"http://mp.weixin.qq.com/s?__biz=MzIxOTYxOTQ4Ng==&mid=2247483780&idx=2&sn=99c5d4bd29dac91a3f3a7ea5c3e48c9b&chksm=97d9c875a0ae4163714a533d5d0863e060edad"},{"ctime":"2016-11-01","title":"【撩妹魔术】手机出票子，请客时惊呆妹子一分钟学会","description":"火热内涵视频","picUrl":"http://mmbiz.qpic.cn/mmbiz_png/5mtyy5fKP9ZR7oFlbOMedVxOSJa4CKY7T2ZDdE1tLjuQKownob83MmWpNGVA9DZDTauv4r1xicro2K7PUwNnS6g/0?wx_fmt=png","url":"http://mp.weixin.qq.com/s?__biz=MzIxOTYxOTQ4Ng==&mid=2247483791&idx=1&sn=2a5d9d3ba98d00640f106bf8adf06f36&chksm=97d9c87ea0ae4168ce406fc3aa16885e6236d8"},{"ctime":"2016-11-01","title":"【撩妹魔术】怎么样口吞硬币从后背拿出来","description":"火热内涵视频","picUrl":"http://mmbiz.qpic.cn/mmbiz_png/5mtyy5fKP9ZR7oFlbOMedVxOSJa4CKY7lL3icrGhcCKQ4ha1qHCQX9ST8EdWvINORXghBppxicibfMW6SdOPwicHaw/0?wx_fmt=png","url":"http://mp.weixin.qq.com/s?__biz=MzIxOTYxOTQ4Ng==&mid=2247483791&idx=2&sn=b9dee39fb5869ce42026d6e69ed17949&chksm=97d9c87ea0ae4168738dc250493b98abf0d080"},{"ctime":"2016-11-01","title":"别在最能吃苦的年纪选择了安逸","description":"分享汇","picUrl":"http://mmbiz.qpic.cn/mmbiz/sZO4ompOicAD1IcbW3Vj1bkLaiafxaibThAF0uVMe0lmoR4Rj3tSnE5kicn50Vt7kkJaHE6WO9uBAqxz29t3doI7lA/0","url":"https://mp.weixin.qq.com/s?__biz=MjM5MjI0ODgzMQ==&mid=200205466&idx=1&sn=b68ff05cbec4a2774f572d6a5b5c6312&mpshare=1&scene=1&srcid=1031ewg3LodsgJEpZir6"},{"ctime":"2016-11-01","title":"最健康的作息时间表","description":"凤煌台联盟","picUrl":"http://mmbiz.qpic.cn/mmbiz/RfeibZHJlMO3DLZ0H8ssS6CfHwXjMNAtzJwHGcSR8ak17VBnXZJPPABMzuEdNazeUh4nqmZKQ7pHKCHwAnf0gIw/0?wx_fmt=jpeg","url":"http://mp.weixin.qq.com/s?__biz=MjM5Mzg2OTI0NQ==&mid=2650990921&idx=1&sn=d26d177cd163e337cdb6bdf790f7e40c&mpshare=1&scene=2&srcid=0929TilMOhksHGZGoV3D"},{"ctime":"2016-10-29","title":"【西电户外\u2022平遥】10月29-30 | 这才是我想告诉你的古城","description":"西电户外","picUrl":"http://mmbiz.qpic.cn/mmbiz_jpg/ulxLDb3dy0kUgEnPZWibBJVdXibvTmXgYaDBx0ibrTCMfElLHdMiaWdFJegEcg1Dys0d7skicMuyK5OwtibAwTKnacVA/0?wx_fmt=jpeg","url":"http://mp.weixin.qq.com/s?__biz=MzI3NzIxNDAwNw==&mid=2649765058&idx=1&sn=fc005d6e5bb0fc0d95369a9b88f99c7b&chksm=f36d329ac41abb8c1bd36deec724c5a7518b5c"},{"ctime":"2016-10-29","title":"微信一大波新功能来袭：互选广告、进群验证、评论回复点赞、热门排行榜\u2026\u2026","description":"微果酱","picUrl":"http://mmbiz.qpic.cn/mmbiz_jpg/52wy0FK91v9g7C5NKZfIGia7Iibz7BrUUVZ0iaVhkfTOAUSiaaJh5mRHUPaEDvowfrC9ibzc3X9UV0lJOBpIuCrpyDA/0?wx_fmt=jpeg","url":"http://mp.weixin.qq.com/s?__biz=MjM5NzEzNzQ3MQ==&mid=2650184674&idx=1&sn=3eabcc4547671ac018714d7a9364908e&chksm=bedca6b289ab2fa4d18de6de3aed6a813d77f8"},{"ctime":"2016-10-29","title":"【推荐】&nbsp;《&nbsp;乌合之众: 大众心理研究》","description":"綦葩说","picUrl":"http://mmbiz.qpic.cn/mmbiz_jpg/jmY3tOJGAAehuIc2fzvtT66oiaY0jlnPQPgXI7kFfC3InnW01ib8aezsia2UAOHFKHbcntf7XGiaibkcsyAwggLHTBQ/0?wx_fmt=jpeg","url":"http://mp.weixin.qq.com/s?__biz=MzI4ODQ0NTA2MQ==&mid=2247484125&idx=1&sn=e1910c0ebbc7e866e7bf490e585faa6f&chksm=ec3f0537db488c2100f2dae9bd15b68d16f700"},{"ctime":"2016-10-29","title":"人活到极致，一定是素与简","description":"刘素云居士","picUrl":"http://mmbiz.qpic.cn/mmbiz_png/wiad52ArWrfpDQO17recU52uVEBicbfPBuT9KWtCr9n2X3MHuXmyUZDMTI1pNIJ4OfTXorj5vzWSSOnZoRBRDmQA/0?wx_fmt=png","url":"http://mp.weixin.qq.com/s?__biz=MjM5ODk2NDUzNg==&mid=2650367208&idx=1&sn=e81ccd8f572c95a83f5709163ea4e572&chksm=becf78bf89b8f1a9d10ed78628e558cfc11ba7"},{"ctime":"2016-10-29","title":"一张图解读工业4.0与互联网的关系","description":"好工坊","picUrl":"http://mmbiz.qpic.cn/mmbiz/ImBRhLUmkb2I45viaEaBVBImHRibk5XqHvJLWToJHxkMXL7uqNFzLwsaKT92LeCeSGCk0WWjicBslltC0LmtXFFuA/0?wx_fmt=jpeg","url":"http://mp.weixin.qq.com/s?__biz=MzAwOTUxMzIwOA==&mid=206516388&idx=1&sn=c851b8c0c3cecf416e5a9268c475a6e6#rd"},{"ctime":"2016-10-29","title":"【科技前沿】什么？！还有你还不知道的3D打印？！","description":"好工坊","picUrl":"http://mmbiz.qpic.cn/mmbiz/ImBRhLUmkb0XTcsN2A8RfgzicBLZzdp40ibMrgwkTibxriczj38TeQ4NcttBRmc04MDwD08dUFPG7wtqATzzBKvAag/0?wx_fmt=jpeg","url":"http://mp.weixin.qq.com/s?__biz=MzAwOTUxMzIwOA==&mid=207376502&idx=1&sn=c554ebfd0b0ac1a24d089e04743897ff#rd"},{"ctime":"2016-10-29","title":"【2015秋冬时尚趋势】摩登与复古的美丽气息","description":"好工坊","picUrl":"http://mmbiz.qpic.cn/mmbiz/ImBRhLUmkb2QdynQ4rzvjLxU4FtDoKwianGaBsbUbKMfZQAWofqHPUBXQSiaXEqEALicvv6OFxUQ8xicibq3fATlIVA/0?wx_fmt=jpeg","url":"http://mp.weixin.qq.com/s?__biz=MzAwOTUxMzIwOA==&mid=207442095&idx=1&sn=edc0a65ca3a438727e0afa53a2e471be#rd"},{"ctime":"2016-10-29","title":"【搭配】穿对衣服，何必要去整容19次？！","description":"好工坊","picUrl":"http://mmbiz.qpic.cn/mmbiz/ImBRhLUmkb2TYvYNzH8ByHsib9r3XxQu3HibvmiasSOUJMWjY57UBQZybtJKgTIfy9RZo1kZvpxj2G1mbQrN3rxZw/0?wx_fmt=jpeg","url":"http://mp.weixin.qq.com/s?__biz=MzAwOTUxMzIwOA==&mid=207453450&idx=1&sn=3a11a0e7b6dad014cfb2bd6ed43fce53#rd"},{"ctime":"2016-10-29","title":"【国民女神变老公？】思聪快来看看小碧这身搭配啊！明明就不是T好么？","description":"好工坊","picUrl":"http://mmbiz.qpic.cn/mmbiz/ImBRhLUmkb2JFVibPRnNVY1EQ3zsnfDd7zQrz3YNG0uHiaAftdz3bHYtuk4gJ8icn9sxBHDsTWUfryN0GIQJHAuLA/0?wx_fmt=jpeg","url":"http://mp.weixin.qq.com/s?__biz=MzAwOTUxMzIwOA==&mid=207497034&idx=2&sn=aea05b93635d9d375f1df15ccf525a63#rd"},{"ctime":"2016-10-29","title":"制造业最需要的不是互联网思维，而是......","description":"好订单","picUrl":"http://mmbiz.qpic.cn/mmbiz/hYVnaDuxcuM9CgNNo6FajvMRuomml31lAoQTiamBI6LnGlueLWsHl33XbkickroAtM1ictk409Dg9kNpbQJPiaouNQ/640?wx_fmt=jpeg","url":"http://mp.weixin.qq.com/s?__biz=MjM5NzYwNTY0MQ==&mid=2651318802&idx=2&sn=3a9ab680fdca4a868e0c7b7a93e8e54e&chksm=bd2432748a53bb623c6046952624eeaf6a7856"},{"ctime":"2016-10-29","title":"工业4.0有多远？","description":"长江商学院MBA","picUrl":"http://mmbiz.qpic.cn/mmbiz/tOXRdCYsyqX3MBdmvKmQibg4RfCVyiaGQibdQJdRX7Yxfy8ic3p3HMgHB82AFb9S1x7DcnzNgfiatF4sCUQopPXepTQ/0?wx_fmt=jpeg","url":"http://mp.weixin.qq.com/s?__biz=MjM5Njc3ODY2MQ==&mid=2650323618&idx=1&sn=ab2374a1693827fbb49a4b42309a175a&scene=0#rd"},{"ctime":"2016-10-20","title":"中国已是赫赫大国，中国人却还不是现代公民","description":"水木然","picUrl":"http://mmbiz.qpic.cn/mmbiz_png/xibK7xmJlMl6mIGZTE3wrp77zicn2wMYM96oFLMMRheLiaHRGH5fhtPSLhJjkWZ3FHB9tXjt56TxVtMw7gezaKgIw/0?wx_fmt=png","url":"http://mp.weixin.qq.com/s?__biz=MzA5NzY4NTQyMw==&mid=2651267428&idx=1&sn=ec8473c928d74c16a75e52665bc33803&chksm=8b6e9ba4bc1912b2955a0f0f24587978682fab"},{"ctime":"2016-10-20","title":"G20之后，中国即将发生的46个重大变化！越读越震惊！","description":"理财中国","picUrl":"http://mmbiz.qpic.cn/mmbiz_jpg/kdRN5kZxWw6MyBic1XgBjsia5tkUhpgKs4lse4KTe9nckFqFXLWwtqS9YOyutxt9IgBEL9OXXYeiaEeCXicSYficiacg/0?wx_fmt=jpeg","url":"http://mp.weixin.qq.com/s?__biz=MjM5NjM0MTI0Mw==&mid=2651751689&idx=2&sn=22c786287bd71ef2f26cf14d40e3503e&scene=1&srcid=090515Fgf374o7lSMK6MARaH#rd"}]
     */

    private int code;
    private String msg;
    /**
     * ctime : 2016-11-01
     * title : 衣服颜色透露了你的性格，which one？
     * description : 好工坊
     * picUrl : http://mmbiz.qpic.cn/mmbiz/ImBRhLUmkb3slXBmKvcnfhU51TlGq3beKY6tFZoWQicTnSDBfiaicic7T7rJsHzplIXGsSJahTbqkOhJfdgCymicibjg/0?wx_fmt=jpeg
     * url : http://mp.weixin.qq.com/s?__biz=MzAwOTUxMzIwOA==&mid=400681065&idx=2&sn=627cf3e25d149c088d50b7d4415afb7b#rd
     */

    private List<NewslistBean> newslist;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<NewslistBean> getNewslist() {
        return newslist;
    }

    public void setNewslist(List<NewslistBean> newslist) {
        this.newslist = newslist;
    }

    public static class NewslistBean {
        private String ctime;
        private String title;
        private String description;
        private String picUrl;
        private String url;

        public String getCtime() {
            return ctime;
        }

        public void setCtime(String ctime) {
            this.ctime = ctime;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
