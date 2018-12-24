package iit.mehul.newsgateway;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.text.DateFormat;
import java.text.ParseException;
import 	java.text.SimpleDateFormat;
import java.util.Date;
import com.squareup.picasso.Picasso;


public class NewsFragment extends Fragment implements View.OnClickListener {

    public static final String FRAG_DATA_ARTICLE = "FRAG_DATA_ARTICLE";
    public static final String INDICE_DATA = "INDICE_DATA";
    public static final String TOTAL_DATA = "TOTAL_DATA";
    private Article article;
    ImageView art_img;

    public static final NewsFragment newInstance(Article article, String indice, String total)
    {
        NewsFragment f = new NewsFragment();
        Bundle bdl = new Bundle(1);
        bdl.putSerializable(FRAG_DATA_ARTICLE, article);
        bdl.putString(INDICE_DATA, indice);
        bdl.putString(TOTAL_DATA, total);
        f.setArguments(bdl);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.news_fragment, container, false);
        article =  (Article) getArguments().getSerializable(FRAG_DATA_ARTICLE);
        String position = getArguments().getString(INDICE_DATA);
        String total = getArguments().getString(TOTAL_DATA);

        TextView article_title = (TextView)v.findViewById(R.id.article_title_ID);
        TextView article_author = (TextView)v.findViewById(R.id.article_author_ID);
        TextView article_date = (TextView)v.findViewById(R.id.article_date_ID);
        TextView article_preview = (TextView)v.findViewById(R.id.article_preview_ID);
        TextView article_count = (TextView)v.findViewById(R.id.article_count_ID);
        art_img = (ImageView)v.findViewById(R.id.article_img_ID);

        article_title.setOnClickListener(this);
        article_preview.setOnClickListener(this);
        art_img.setOnClickListener(this);

        article_title.setText(article.getTitleName().replace("null",""));
        article_title.setText(article.getTitleName().replace("null",""));
        article_author.setText(article.getAuthorName().replace("null",""));
        article_preview.setText(article.getNewsDescData().replace("null",""));
        article_count.setText(Integer.parseInt(position)+1 + " of " + total);
        if(article.getDateTimeValue()!= null) {
            DateFormat fromFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            fromFormat.setLenient(false);
            DateFormat toFormat = new SimpleDateFormat("MMM dd,yyyy hh:mmaa");
            toFormat.setLenient(false);
            String dateStr = article.getDateTimeValue();
            Date date;

            int count = 0;
            int maxTries = 2;
            boolean pass = false;
            while(!pass) {
                try {
                    date = fromFormat.parse(dateStr);
                    article_date.setText(toFormat.format(date));
                    pass = true;
                } catch (ParseException e) {
                    fromFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");
                    if (++count == maxTries) {
                        pass=true;
                        article_date.setText("");
                    }
                }
            }

        }
        System.out.println("length :" +article.getUrlImage().length());
        if(article.getUrlImage().length()>0)
        loadPhoto(article.getUrlImage(), v);
        else
            loadPhoto("null", v);

        return v;
    }



    private void loadPhoto(String url, View v){

        Picasso picasso = new Picasso.Builder(this.getContext())
                .listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        picasso.load(R.drawable.brokenimage)
                                .into(art_img);
                    }
                })
                .build();

        picasso.load(url)
                .error(R.drawable.brokenimage)
                .placeholder(R.drawable.placeholder)
                .into(art_img);
    }

    @Override
    public void onClick(View v) {
        String url = article.getApiAddressDetail();
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}