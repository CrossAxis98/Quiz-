package com.arianameble.quiz;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FirstFragment extends Fragment {

    private static final String TAG = "FirstFragment";

    private static final int FURNITURES_IN_QUIZ = 10;

    private List<String> fileNameLists;

    private List<String> quizFurnituresList;

    private Set<String> categoriesSet;

    private String correctAnswer;

    private int totalGuessess;

    private int correctAnswers;

    private int guessRows;

    private SecureRandom random;

    private Handler handler;

    private Animation shakeAnimation;

    private LinearLayout quizLinearLayout;

    private TextView questionNumberTextView;

    private ImageView furnitureImageView;

    private LinearLayout[] guessLinearLayouts;

    private TextView answerTextView;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
       super.onCreateView(inflater, container, savedInstanceState);

       View view = inflater.inflate(R.layout.fragment_first, container, false);

       fileNameLists = new ArrayList<>();
       quizFurnituresList = new ArrayList<>();
       random = new SecureRandom();
       handler = new Handler();

       shakeAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.incorrect_shake);
       shakeAnimation.setRepeatCount(3);

       quizLinearLayout = (LinearLayout) view.findViewById(R.id.quizLinearLayout);
       questionNumberTextView = (TextView) view.findViewById(R.id.questionNumberTextView);
       furnitureImageView = (ImageView) view.findViewById(R.id.furnitureImageView);
       guessLinearLayouts = new LinearLayout[4];
       guessLinearLayouts[0] = (LinearLayout) view.findViewById(R.id.row1LinearLayout);
       guessLinearLayouts[1] = (LinearLayout) view.findViewById(R.id.row2LinearLayout);
       guessLinearLayouts[2] = (LinearLayout) view.findViewById(R.id.row3LinearLayout);
       guessLinearLayouts[3] = (LinearLayout) view.findViewById(R.id.row4LinearLayout);
       answerTextView = (TextView) view.findViewById(R.id.answerTextView);

       for(LinearLayout row: guessLinearLayouts) {
           for (int column = 0; column < row.getChildCount(); column++) {
               Button button = (Button) row.getChildAt(column);
               button.setOnClickListener(guessButtonListener);
           }
       }

       questionNumberTextView.setText(getString(R.string.question, 1, FURNITURES_IN_QUIZ));

       return view;
    }



    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}
