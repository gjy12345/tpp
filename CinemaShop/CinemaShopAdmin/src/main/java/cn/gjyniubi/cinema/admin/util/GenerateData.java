package cn.gjyniubi.cinema.admin.util;

import cn.gjyniubi.cinema.admin.core.doc.film.mapper.FilmDocMapper;
import cn.gjyniubi.cinema.admin.core.doc.type.mapper.DocFilmTypeMapper;
import cn.gjyniubi.cinema.common.contact.UserContact;
import cn.gjyniubi.cinema.common.entry.DocFilm;
import cn.gjyniubi.cinema.common.entry.DocFilmType;
import cn.gjyniubi.cinema.common.entry.FilmType;
import cn.gjyniubi.cinema.common.mapper.CommonFilmTypesMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Author gujianyang
 * @Date 2021/5/25
 * @Class GenerateData
 */
@Component
public class GenerateData {

    @Autowired
    private FilmDocMapper filmDocMapper;

    @Autowired
    private DocFilmTypeMapper typeMapper;

    @Autowired
    private CommonFilmTypesMapper commonFilmTypesMapper;

    @Value("${project.file.save-path}")
    private String saveDirPath;

    private final static SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");

    public void generate() throws Exception {
        String hot = "https://dianying.taobao.com/showList.htm?spm=a1z21.3046609.header.4.32c0112az5UcKq&n_s=new";
        final Document html = Jsoup.connect(hot)
                .header("User-Agent","Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0;")
                .header("referer","https://dianying.taobao.com/")
                .get();
        Elements movies = html.select(".movie-card-wrap");
        for (Element movie : movies) {
            String name = StringUtils.deleteWhitespace(movie.selectFirst(".movie-card-name").text().trim());
            String pf = movie.selectFirst("span.bt-r").text();
            String url = movie.selectFirst("a").attr("href");
            name = StringUtils.deleteWhitespace(name.replace(pf,""));
            System.out.println(name+" "+url);
            if(filmDocMapper.selectCount(new QueryWrapper<DocFilm>().eq("name",name))>0){
                System.out.println("已存在");
                Thread.sleep(2000);
                continue;
            }
            Thread.sleep(2000);
            readValue(url, hot);
            Thread.sleep(10000);
        }
    }

    private void readValue(String url,String hot) throws Exception {
        final Document html = Jsoup.connect(url)
                .header("User-Agent","Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0;")
                .header("referer",hot)
                .get();
        try {
            String showTime = html.selectFirst("body > div.detail-wrap.J_detailWrap > div.detail-cont > div > div.cont-time")
                    .text().split("：")[1];
            String name = safeGetText(html.selectFirst("body > div.detail-wrap.J_detailWrap > div.detail-cont > div > h3"));
            String score = safeGetText(html.selectFirst("body > div.detail-wrap.J_detailWrap > div.detail-cont > div > h3 > em"));
            String dy = safeGetValue(html.selectFirst("body > div.detail-wrap.J_detailWrap > div.detail-cont > div > ul > li:contains(导演)"),1);
            String star = safeGetValue(html.selectFirst("body > div.detail-wrap.J_detailWrap > div.detail-cont > div > ul > li:contains(主演)"),1);
            String type = safeGetValue(html.selectFirst("body > div.detail-wrap.J_detailWrap > div.detail-cont > div > ul > li:contains(类型)"),1);
            String region = safeGetValue(html.selectFirst("body > div.detail-wrap.J_detailWrap > div.detail-cont > div > ul > li:contains(地区)"),1);
            String time = safeGetValue(html.selectFirst("body > div.detail-wrap.J_detailWrap > div.detail-cont > div > ul > li:contains(片长)"),1);
            String info = safeGetValue(html.selectFirst("body > div.detail-wrap.J_detailWrap > div.detail-cont > div > ul > li.J_shrink.shrink"),1);
            Element temp = html.selectFirst("body > div.detail-wrap.J_detailWrap > div.detail-cont > div > div.cont-pic > img");
            String cover = temp==null?"":temp.absUrl("src");
            DocFilm docFilm = new DocFilm();
            docFilm.setStatus(UserContact.STATUS_ACTIVE);
            docFilm.setCreateTime(new Date());
            docFilm.setUpdateTime(docFilm.getCreateTime());
            docFilm.setLogicDel(UserContact.LOGIC_DEL_NO_DELETE);
            docFilm.setCreateBy(1);
            docFilm.setUid(UUID.randomUUID().toString());
            docFilm.setName(name.replace(score,""));
            docFilm.setShowTime(format.parse(showTime));
            docFilm.setDirector(dy);
            docFilm.setStar(star);
            docFilm.setRegion(region);
            docFilm.setDuration(time);
            docFilm.setDescribe(info);
            docFilm.setCover(cover);
            docFilm.setScore(score);
            docFilm.setCover(downloadCover(cover));
            filmDocMapper.insert(docFilm);
            String[] types = type.split(",");
            for (String t : types) {
                if(typeMapper.selectCount(new QueryWrapper<DocFilmType>().eq("name",t))==0){
                    DocFilmType dt = new DocFilmType();
                    dt.setLogicDel(UserContact.LOGIC_DEL_NO_DELETE);
                    dt.setStatus(UserContact.STATUS_ACTIVE);
                    dt.setName(t);
                    dt.setCreateBy(1);
                    dt.setCreateTime(new Date());
                    dt.setUpdateTime(dt.getCreateTime());
                    typeMapper.insert(dt);
                }
            }
            List<DocFilmType> filmTypes = typeMapper.selectList(new QueryWrapper<DocFilmType>().in("name", Arrays.asList(types)));
            //插入类型
            System.out.println(docFilm);
            FilmType ft = new FilmType();
            for (DocFilmType filmType : filmTypes) {
                ft.setFilmTypeId(filmType.getId());
                ft.setFilmId(docFilm.getId());
                commonFilmTypesMapper.insert(ft);
            }
        }catch (Exception e){
            System.out.println(html);
            throw e;
        }
    }

    private String safeGetText(Element element) {
        return element==null?"":element.text();
    }

    private String safeGetValue(Element e, int index){
        if(e==null)
            return "";
        String[] arr=e.text().split("：");
        if(index>=arr.length){
            return arr[arr.length-1];
        }
        return arr[index];
    }

    private String downloadCover(String u) throws IOException {
        try {
            //https://img.alicdn.com/bao/uploaded/i4/O1CN01WHCSYs1aO9DIjdTwp_!!6000000003319-0-alipicbeacon.jpg_300x300.jpg
            URL url = new URL(u);
            URLConnection conn = url.openConnection();
            //设置超时间为3秒
            conn.setConnectTimeout(3 * 1000);
            //防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            //输出流
            InputStream str = conn.getInputStream();
            //控制流的大小为1k
            byte[] bs = new byte[1024];
            //读取到的长度
            int len = 0;
            //是否需要创建文件夹
            File saveDir = new File(saveDirPath);
            if (!saveDir.exists()) {
                saveDir.mkdir();
            }
            File file = new File(saveDirPath,UUID.randomUUID().toString()+u.substring(u.lastIndexOf(".")));
            //实例输出一个对象
            FileOutputStream out = new FileOutputStream(file);
            //循环判断，如果读取的个数b为空了，则is.read()方法返回-1，具体请参考InputStream的read();
            while ((len = str.read(bs)) != -1) {
                //将对象写入到对应的文件中
                out.write(bs, 0, len);
            }
            //刷新流
            out.flush();
            //关闭流
            out.close();
            str.close();
            System.out.println("下载成功");
            return file.getName();
        }catch (Exception e){
            e.printStackTrace();
        }
        return u;
    }

}
