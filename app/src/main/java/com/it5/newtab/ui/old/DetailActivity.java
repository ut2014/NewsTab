package com.it5.newtab.ui.old;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.view.KeyEvent;

import com.it5.newtab.R;
import com.it5.newtab.base.BaseActivity;
import com.it5.newtab.emoji.OnSendClickListener;
import com.it5.newtab.emoji.ToolbarFragment;
import com.it5.newtab.viewpagerfragm.old_frag.BaseFragment;
import com.it5.newtab.viewpagerfragm.old_frag.CommentFragment;

/**
 * 详情activity（包括：资讯、博客、软件、问答、动弹）
 * Created by IT5 on 2016/11/3.
 */

public class DetailActivity extends BaseActivity implements OnSendClickListener {
    public static final int DISPLAY_NEWS = 0;
    public static final int DISPLAY_BLOG = 1;
    public static final int DISPLAY_SOFTWARE = 2;
    public static final int DISPLAY_POST = 3;
    public static final int DISPLAY_TWEET = 4;
    public static final int DISPLAY_EVENT = 5;
    public static final int DISPLAY_TEAM_ISSUE_DETAIL = 6;
    public static final int DISPLAY_TEAM_DISCUSS_DETAIL = 7;
    public static final int DISPLAY_TEAM_TWEET_DETAIL = 8;
    public static final int DISPLAY_TEAM_DIARY = 9;
    public static final int DISPLAY_COMMENT = 10;

    public static final String BUNDLE_KEY_DISPLAY_TYPE = "BUNDLE_KEY_DISPLAY_TYPE";


    private OnSendClickListener currentFragment;
    public KJEmojiFragment emojiFragment = new KJEmojiFragment();
    public ToolbarFragment toolFragment = new ToolbarFragment();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_detail;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.actionbar_title_detail;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        int displayType = getIntent().getIntExtra(BUNDLE_KEY_DISPLAY_TYPE,
                DISPLAY_NEWS);
        BaseFragment fragment = null;
        int actionBarTitle = 0;
        switch (displayType) {
            case DISPLAY_NEWS:
                actionBarTitle = R.string.actionbar_title_news;
                fragment = new NewsDetailFragment();
                break;
            case DISPLAY_EVENT:
                actionBarTitle = R.string.actionbar_title_event_detail;
                fragment = new EventDetailFragment();
                break;
            default:
                break;
        }
        setActionBarTitle(actionBarTitle);
        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        if (fragment == null) {
            return;
        }
        trans.replace(R.id.container, fragment);
        trans.commitAllowingStateLoss();
        if (fragment instanceof OnSendClickListener) {
            currentFragment = (OnSendClickListener) fragment;
        } else {
            currentFragment = new OnSendClickListener() {
                @Override
                public void onClickSendButton(Editable str) {

                }

                @Override
                public void onClickFlagButton() {

                }
            };
        }
    }

    @Override
    public void initView() {
        /*if (currentFragment instanceof TweetDetailFragment
                || currentFragment instanceof TeamTweetDetailFragment
                || currentFragment instanceof TeamDiaryDetailFragment
                || currentFragment instanceof TeamIssueDetailFragment
                || currentFragment instanceof TeamDiscussDetailFragment
                || currentFragment instanceof CommentFragment) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.emoji_keyboard, emojiFragment).commit();
        } else */
        if (currentFragment instanceof CommentFragment) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.emoji_keyboard, emojiFragment).commit();
        } else
        {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.emoji_keyboard, toolFragment).commit();
        }

        toolFragment.setOnActionClickListener(new ToolbarFragment.OnActionClickListener()

                                              {
                                                  @Override
                                                  public void onActionClick(ToolbarFragment.ToolAction action) {
                                                      switch (action) {
                                                          case ACTION_CHANGE:
                                                          case ACTION_WRITE_COMMENT:
                                                              getSupportFragmentManager()
                                                                      .beginTransaction()
                                                                      .setCustomAnimations(R.anim.footer_menu_slide_in,
                                                                              R.anim.footer_menu_slide_out)
                                                                      .replace(R.id.emoji_keyboard, emojiFragment)
                                                                      .commit();
                                                              break;
                                                          case ACTION_FAVORITE:
                                                              ((CommonDetailFragment) currentFragment)
                                                                      .handleFavoriteOrNot();
                                                              break;
                                                          case ACTION_REPORT:
                                                              ((CommonDetailFragment) currentFragment).onReportMenuClick();
                                                              break;
                                                          case ACTION_SHARE:
                                                              ((CommonDetailFragment) currentFragment).handleShare();
                                                              break;
                                                          case ACTION_VIEW_COMMENT:
                                                              ((CommonDetailFragment) currentFragment)
                                                                      .onCilckShowComment();
                                                              break;
                                                          default:
                                                              break;
                                                      }
                                                  }
                                              }

        );

    }

    @Override
    public void initData() {
    }

    @Override
    public void onClickSendButton(Editable str) {
        currentFragment.onClickSendButton(str);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            try {
                if (emojiFragment.isShowEmojiKeyBoard()) {
                    emojiFragment.hideAllKeyBoard();
                    return true;
                }
                if (emojiFragment.getEditText().getTag() != null) {
                    emojiFragment.getEditText().setTag(null);
                    emojiFragment.getEditText().setHint("说点什么吧");
                    return true;
                }
            } catch (NullPointerException e) {
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setCommentCount(int count) {
        try {
            toolFragment.setCommentCount(count);
        } catch (Exception e) {
        }
    }

    @Override
    public void onClickFlagButton() {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.footer_menu_slide_in,
                        R.anim.footer_menu_slide_out)
                .replace(R.id.emoji_keyboard, toolFragment).commit();
        try {

        } catch (Exception e) {
        }
    }
}
