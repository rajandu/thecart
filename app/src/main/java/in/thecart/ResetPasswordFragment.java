package in.thecart;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordFragment extends Fragment {

    public ResetPasswordFragment(){

    }

    private EditText registeredEmail;
    private Button resetPasswordButton;
    private TextView goBack;
    private ImageView emailIcon;
    private TextView emailSent;

    private FrameLayout parentFrameLayout;

    private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);

        registeredEmail = view.findViewById(R.id.editTextForget);
        resetPasswordButton = view.findViewById(R.id.buttonForget);
        goBack = view.findViewById(R.id.tvGoBack);
        emailIcon = view.findViewById(R.id.emailIconForgetPassword);
        emailSent = view.findViewById(R.id.emailSentTv);

        parentFrameLayout = getActivity().findViewById(R.id.register_frameLayout);

        firebaseAuth = FirebaseAuth.getInstance();


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        registeredEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new SignInFragment());
            }
        });

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.sendPasswordResetEmail(registeredEmail.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    emailIcon.setVisibility(View.VISIBLE);
                                    emailSent.setVisibility(View.VISIBLE);
                                }else{
                                    String error = task.getException().toString();
                                    Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    private void checkInputs()
    {
        if(TextUtils.isEmpty(registeredEmail.getText())){
            resetPasswordButton.setEnabled(false);
        }else {
            resetPasswordButton.setEnabled(true);
        }
    }

    private void setFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left,R.anim.slideout_from_right);
        fragmentTransaction.replace(parentFrameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }
}