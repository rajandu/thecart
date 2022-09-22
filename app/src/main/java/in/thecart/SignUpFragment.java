package in.thecart;

import android.content.Intent;
import android.graphics.Color;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {

    public SignUpFragment()
    {

    }

    private TextView alreadyHaveAnAccount;
    private FrameLayout parentFrameLayout;

    private EditText email;
    private EditText fullName;
    private EditText password;
    private EditText confirmPassword;
    
    private Button signUpBtn;
    
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        alreadyHaveAnAccount = view.findViewById(R.id.alredyAccountSignUpView);
        parentFrameLayout = getActivity().findViewById(R.id.register_frameLayout);
        
        email = view.findViewById(R.id.editTextEmailSignup);
        fullName = view.findViewById(R.id.editTextUserNameSignup);
        password = view.findViewById(R.id.editTextPasswordSignup);
        confirmPassword = view.findViewById(R.id.editTextConfirmPasswordSignup);
        signUpBtn = view.findViewById(R.id.buttonRegister);
        
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        alreadyHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new SignInFragment());
            }
        });
        
        email.addTextChangedListener(new TextWatcher() {
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
        fullName.addTextChangedListener(new TextWatcher() {
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
        password.addTextChangedListener(new TextWatcher() {
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
        confirmPassword.addTextChangedListener(new TextWatcher() {
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
        
        
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO send data to firebase
                
                checkEmailAndPassword();
            }
        });
        
        
    }

    private void checkEmailAndPassword() {

        if(email.getText().toString().matches(emailPattern)){
            if(password.getText().toString().equals(confirmPassword.getText().toString())){

                signUpBtn.setEnabled(false);

                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){

                                  final  Map<Object,String> userdata = new HashMap<>();
                                    userdata.put("name",fullName.getText().toString());
                                    userdata.put("email",email.getText().toString());
                                    userdata.put("profile","");

                                    firebaseFirestore.collection("USERS")
                                            .add(userdata)
                                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentReference> task) {

                                                    if(task.isSuccessful())
                                                    {
                                                        CollectionReference userDataReference=firebaseFirestore.collection("USERS").document(firebaseAuth.getUid()).collection("USER_DATA");

                                                        Map<String,Object> wishlistMap= new HashMap<>();
                                                        wishlistMap.put("list_size", (long) 0);

                                                        Map<String,Object> ratingsMap= new HashMap<>();
                                                        ratingsMap.put("list_size", (long) 0);

                                                        Map<String,Object> cartMap= new HashMap<>();
                                                        cartMap.put("list_size", (long) 0);

                                                        Map<String,Object> myAddressesMap= new HashMap<>();
                                                        myAddressesMap.put("list_size", (long) 0);

                                                        Map<String,Object> notificationMap= new HashMap<>();
                                                        notificationMap.put("list_size", (long) 0);

                                                        List<String> documentNames = new ArrayList<>();
                                                        documentNames.add("MY_WISHLIST");
                                                        documentNames.add("MY_RATINGS");
                                                        documentNames.add("MY_CART");
                                                        documentNames.add("MY_ADDRESSES");
                                                        documentNames.add("MY_NOTIFICATIONS");

                                                        final List<Map<String,Object>> documentFields = new ArrayList<>();
                                                        documentFields.add(wishlistMap);
                                                        documentFields.add(ratingsMap);
                                                        documentFields.add(cartMap);
                                                        documentFields.add(myAddressesMap);
                                                        documentFields.add(notificationMap);

                                                        for (int x=0; x<documentNames.size() ;x++){
                                                            final int finalX = x;
                                                            userDataReference.document(documentNames.get(x))
                                                                    .set(documentFields.get(x))
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if(task.isSuccessful()){
                                                                                if(finalX == documentFields.size()-1) {

                                                                                    startActivity(new Intent(getActivity(), MainActivity.class));
                                                                                    getActivity().finish();
                                                                                }
                                                                            }else {
                                                                                signUpBtn.setEnabled(true);
                                                                                signUpBtn.setTextColor(Color.rgb(255,255,255));
                                                                                String error=task.getException().getMessage();
                                                                                Toast.makeText(getActivity(), error,Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }
                                                                    });
                                                        }
                                                        //setFragment(new SignInFragment());
                                                    }else{
                                                        signUpBtn.setEnabled(true);
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }else{
                                    String error = task.getException().toString();
                                    Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }else {
                confirmPassword.setError("Password doesn't matched!");
            }
        }else{
            email.setError("Invalid Email");
        }
    }

    private void setFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left,R.anim.slideout_from_right);
        fragmentTransaction.replace(parentFrameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }
    
    private void checkInputs()
    {
        if(!TextUtils.isEmpty(fullName.getText())) {
            if(!TextUtils.isEmpty(email.getText())){
                if (!TextUtils.isEmpty(password.getText()) && password.length() >= 0){
                    if(!TextUtils.isEmpty(confirmPassword.getText())){
                        signUpBtn.setEnabled(true);
                    }else{
                        signUpBtn.setEnabled(false);
                }
            }else{
                signUpBtn.setEnabled(false);
            }
        }else{
            signUpBtn.setEnabled(false);
        }
    }else{
            signUpBtn.setEnabled(false);
        }
    }
}