package com.example.quizam;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
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

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
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

    public void updateGuessRows(SharedPreferences sharedPreferences) {
        String choices = sharedPreferences.getString(MainActivity.CHOICES, null);

        guessRows = Integer.parseInt(choices) / 2;

        for (LinearLayout layout : guessLinearLayouts) {
            layout.setVisibility(View.GONE);
        }

        for (int row = 0; row < guessRows; row++)
        {
            guessLinearLayouts[row].setVisibility(View.VISIBLE);
        }
    }

    public void updateCategories(SharedPreferences sharedPreferences){
        categoriesSet = sharedPreferences.getStringSet(MainActivity.CATEGORIES, null);
    }

    public void resetQuiz(){

        AssetManager assets = getActivity().getAssets();

        fileNameLists.clear();

        try {
            for(String category: categoriesSet) {
                String[] paths = assets.list(category);

                for (String path : paths)
                {
                    fileNameLists.add(path.replace(".jpg", ""));
                }
            }

        }catch(IOException ex)
        {
            Log.d(TAG, "resetQuiz: błąd podczas ładowania plików z obrazami", ex);
        }

        correctAnswers = 0;
        totalGuessess = 0;

        quizFurnituresList.clear();

        int furnitureCounter = 1;
        int numberOfFurnitures = fileNameLists.size();

        while(furnitureCounter <= FURNITURES_IN_QUIZ)
        {
            int randomIndex = random.nextInt(numberOfFurnitures);

            String fileName = fileNameLists.get(randomIndex);

            if(!quizFurnituresList.contains(fileName)){
                quizFurnituresList.add(fileName);
                ++furnitureCounter;
            }
        }

        loadNextFurniture();

    }

    private void loadNextFurniture() {
        String nextImage = quizFurnituresList.remove(0);

        correctAnswer = nextImage;

        answerTextView.setText("");

        questionNumberTextView.setText(getString(R.string.question, (correctAnswers + 1), FURNITURES_IN_QUIZ));

        String category = nextImage.substring(0, nextImage.indexOf("-"));

        AssetManager assets = getActivity().getAssets();

        try (InputStream inputStreamFurniture = assets.open(category + "/" + nextImage + ".jpg")) {

            Drawable drawableFurniture = Drawable.createFromStream(inputStreamFurniture, nextImage);

            furnitureImageView.setImageDrawable(drawableFurniture);

            animate(false);

        } catch (IOException ex) {
            Log.d(TAG, "loadNextFurniture: błąd podczas ładowania" + nextImage, ex);
        }

        Collections.shuffle(fileNameLists);

        int correct = fileNameLists.indexOf(correctAnswer);
        fileNameLists.add(fileNameLists.remove(correct));

        for (int row = 0; row < guessRows; row++) {
            for (int column = 0; column < 2; column++)
            {
                Button guessButton = (Button) guessLinearLayouts[row].getChildAt(column);
                guessButton.setEnabled(true);

                String fileName = fileNameLists.get((row * 2)+column);
                guessButton.setText(getFurnitureName(fileName));
            }
        }

        int row = random.nextInt(guessRows);
        int column = random.nextInt(2);
        LinearLayout randomRow = guessLinearLayouts[row];
        String furnitureName = getFurnitureName(correctAnswer);
        ((Button) randomRow.getChildAt(column)).setText(furnitureName);
    }


    private String getFurnitureName(String name)
    {
        return name.substring(name.indexOf("-")+1).replace("-", " ");
    }

    private void animate(boolean animateOut)
    {
        if(correctAnswers == 0) return;

        int centerX = (quizLinearLayout.getLeft() + quizLinearLayout.getRight())/2;
        int centerY = (quizLinearLayout.getTop() + quizLinearLayout.getBottom())/2;

        int radious = Math.max(quizLinearLayout.getWidth(), quizLinearLayout.getHeight());

        Animator animator;

        if(animateOut){

            animator = ViewAnimationUtils.createCircularReveal(quizLinearLayout, centerX, centerY, radious, 0);

            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    loadNextFurniture();
                }
            });

        }
        else {
            animator = ViewAnimationUtils.createCircularReveal(quizLinearLayout, centerX, centerY, 0, radious);
        }

        animator.setDuration(500);

        animator.start();
    }

    private View.OnClickListener guessButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button guessButton = (Button) v;
            String guess = guessButton.getText().toString();
            String answer = getFurnitureName(correctAnswer);

            ++totalGuessess;

            if(guess.equals(answer)){
                ++correctAnswers;

                answerTextView.setText(answer + "!");
                answerTextView.setTextColor(getResources().getColor(R.color.correct_answer, getContext().getTheme()));
                disableButtons();

                if(correctAnswers == FURNITURES_IN_QUIZ){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Quiz results");
                    builder.setMessage(getString(R.string.results, totalGuessess, (1000/(double) totalGuessess)));
                    builder.setPositiveButton(R.string.reset_quiz, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            resetQuiz();
                        }
                    });

                    builder.setCancelable(false);
                    builder.show();
                }
                else {

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            animate(true);
                        }
                    }, 2000);

                }
            }
            else
            {
                furnitureImageView.startAnimation(shakeAnimation);
                answerTextView.setText(R.string.incorrect_answer);
                answerTextView.setTextColor(getResources().getColor(R.color.incorrect_answer, getContext().getTheme()));

                guessButton.setEnabled(false);
            }
        }
    };


    private void disableButtons(){
        for (int row = 0; row < guessRows; row++){
            LinearLayout guessRow = guessLinearLayouts[row];
            for ( int column = 0; column < 2; column++){
                guessRow.getChildAt(column).setEnabled(false);
            }
        }
    }







    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}

