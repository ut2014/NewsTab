package com.it5.newtab.adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.it5.newtab.AppContext;
import com.it5.newtab.R;
import com.it5.newtab.old_been.ListBaseAdapter;
import com.it5.newtab.old_been.News;
import com.it5.newtab.old_been.NewsList;
import com.it5.newtab.util.StringUtils;
import com.it5.newtab.util.ThemeSwitchUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsAdapter extends ListBaseAdapter<News> {

    @SuppressLint("InflateParams")
    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null || convertView.getTag() == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(
                    R.layout.list_cell_news, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        News news = mDatas.get(position);
        vh.title.setText(news.getTitle());

        if (AppContext.isOnReadedPostList(NewsList.PREF_READED_NEWS_LIST,
                news.getId() + "")) {
            vh.title.setTextColor(parent.getContext().getResources()
                    .getColor(ThemeSwitchUtils.getTitleReadedColor()));
        } else {
            vh.title.setTextColor(parent.getContext().getResources()
                    .getColor(ThemeSwitchUtils.getTitleUnReadedColor()));
        }

        String description = news.getBody();
        vh.description.setVisibility(View.GONE);
        if (description != null && !StringUtils.isEmpty(description)) {
            vh.description.setVisibility(View.VISIBLE);
            vh.description.setText(description.trim());
        }

        vh.source.setText(news.getAuthor());
        if (StringUtils.isToday(news.getPubDate())) {
            vh.tip.setVisibility(View.VISIBLE);
        } else {
            vh.tip.setVisibility(View.GONE);
        }
        vh.time.setText(StringUtils.friendly_time(news.getPubDate()));
        vh.comment_count.setText(news.getCommentCount() + "");

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_title)
        TextView title;
        @BindView(R.id.tv_description)
        TextView description;
        @BindView(R.id.tv_source)
        TextView source;
        @BindView(R.id.tv_time)
        TextView time;
        @BindView(R.id.tv_comment_count)
        TextView comment_count;
        @BindView(R.id.iv_tip)
        ImageView tip;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
