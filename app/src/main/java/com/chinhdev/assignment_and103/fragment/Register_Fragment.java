package com.chinhdev.assignment_and103.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.chinhdev.assignment_and103.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register_Fragment extends Fragment {

    FirebaseAuth auth;
    TextInputEditText edt_email, edt_pass, edt_repass;
    Button bt_register;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        auth = FirebaseAuth.getInstance();
        edt_email = view.findViewById(R.id.ed_email_regis);
        edt_pass = view.findViewById(R.id.ed_password_regis);
        edt_repass = view.findViewById(R.id.ed_repassword_regis);
        bt_register = view.findViewById(R.id.bt_register);
        bt_register.setOnClickListener(v->{
            String email = edt_email.getText().toString();
            String pass = edt_pass.getText().toString();
            auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        Toast.makeText(getContext(), "Đăng ky thanh cong", Toast.LENGTH_SHORT).show();
                        Bundle bundle = new Bundle();
                        bundle.putString("user_email", email);
                        bundle.putString("password", pass);

                        Login_Fragment loginFragment = new Login_Fragment();
                        loginFragment.setArguments(bundle);

                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, loginFragment)
                                .commit();
                    } else {
                        Toast.makeText(getContext(), "Dang ky that bai", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });
    }
}