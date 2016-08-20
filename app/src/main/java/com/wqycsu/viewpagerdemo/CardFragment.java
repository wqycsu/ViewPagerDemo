package com.wqycsu.viewpagerdemo;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.util.LruCache;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by wqycsu on 16/8/20.
 */
public class CardFragment extends android.support.v4.app.DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    private static final String TAG = "CardFragment";
    private static final String ARG_PARAM1 = "param1";
    private RelativeLayout relativeLayout;
    private ViewPager viewPager;
    private Button closeBtn;
    // TODO: Rename and change types of parameters
    private ArrayList<Integer> mParam1;
    private ViewPagerAdapter adapter;
    private LruCache<Integer, View> cachePagerItem = new LruCache<Integer, View>(5);
    private Bitmap background;

    public CardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CardFragment newInstance(ArrayList<Integer> urls) {
        CardFragment fragment = new CardFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, urls);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(CardFragment.STYLE_NO_TITLE, android.R.style.Theme_Black_NoTitleBar);
        if (getArguments() != null) {
            mParam1 = (ArrayList<Integer>) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_card_dialog, container, false);
        if(background != null) {
            getDialog().getWindow().setBackgroundDrawable(new BitmapDrawable(background));
        }
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.viewpager_container);
        closeBtn = (Button) view.findViewById(R.id.close_btn);
        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return viewPager.onTouchEvent(event);
            }
        });
        viewPager.setOffscreenPageLimit(3);
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        viewPager.setPageMargin((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, metrics));
        viewPager.setPageTransformer(true, new MyPageTransform());
        adapter = new ViewPagerAdapter(mParam1);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CardFragment.this.dismiss();
            }
        });
        return view;
    }

    public void updateDataSet(int index) {
        if(!Thread.currentThread().equals(Looper.getMainLooper().getThread())) {
            if (cachePagerItem != null) {
                synchronized (cachePagerItem) {
                    ImageView imageView = (ImageView) cachePagerItem.get(index);
                    if (imageView != null) {
                        imageView.setImageResource(adapter.getImages().get(index));
                    }
                }
            }
        } else {
            if (cachePagerItem != null) {
                synchronized (cachePagerItem) {
                    ImageView imageView = (ImageView) cachePagerItem.get(index);
                    if (imageView != null) {
                        imageView.setImageResource(adapter.getImages().get(index));
                    }
                }
            }
        }
    }

    class ViewPagerAdapter extends PagerAdapter {

        private ArrayList<Integer> images;

        public ViewPagerAdapter(ArrayList<Integer> images) {
            this.images = images;
        }

        public ArrayList<Integer> getImages() {
            return images;
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_UNCHANGED;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((ImageView) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView imageView = (ImageView) cachePagerItem.get(position);
            if(imageView == null) {
                imageView = (ImageView) LayoutInflater.from(getContext()).inflate(R.layout.viewpager_item, null);
                cachePagerItem.put(position, imageView);
            }
            if(images.get(position) == 0) {
                imageView.setImageResource(R.mipmap.ic_launcher);
            } else {
                imageView.setImageResource(images.get(position));
            }
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "点击了:" + position, Toast.LENGTH_LONG).show();
                }
            });
            container.addView(imageView);
            return imageView;
        }
    }

    class MyPageTransform implements ViewPager.PageTransformer {

        final float SCALE_MAX = 0.8f;
        final float ALPHA_MAX = 0.5f;

        @Override
        public void transformPage(View page, float position) {
            if((int)position < - 1 || (int)position > 1) {
                return;
            }

            float scale = (position < 0)
                    ? ((1 - SCALE_MAX) * position + 1)
                    : ((SCALE_MAX - 1) * position + 1);
            float alpha = (position < 0)
                    ? ((1 - ALPHA_MAX) * position + 1)
                    : ((ALPHA_MAX - 1) * position + 1);
            if(position < 0) {
                ViewCompat.setPivotX(page, page.getWidth());
                ViewCompat.setPivotY(page, page.getHeight() / 2);
            } else {
                ViewCompat.setPivotX(page, 0);
                ViewCompat.setPivotY(page, page.getHeight() / 2);
            }
            Log.d(TAG, "position: " + position + ",scale:" + scale);

            ViewCompat.setScaleX(page, scale);
            ViewCompat.setScaleY(page, scale);
            ViewCompat.setAlpha(page, Math.abs(alpha));
        }
    }

    public void setBackground(Bitmap background) {
        this.background = background;
    }

}
