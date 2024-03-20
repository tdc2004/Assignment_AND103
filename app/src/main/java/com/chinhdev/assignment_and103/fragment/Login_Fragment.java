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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chinhdev.assignment_and103.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class Login_Fragment extends Fragment {
    TextView tv_register;
    EditText edt_email,edt_pass;
    Button btn_login;
    FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv_register = view.findViewById(R.id.tv_signUp);
        edt_email = view.findViewById(R.id.ed_email_login);
        edt_pass = view.findViewById(R.id.ed_password_login);
        btn_login = view.findViewById(R.id.bt_login);
        auth = FirebaseAuth.getInstance();
        tv_register.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Register_Fragment()).commit();
        });
        Bundle bundle = getArguments();
        if (bundle != null) {
            String userEmail = bundle.getString("user_email");
            String password = bundle.getString("password");

            edt_email.setText(userEmail);
            edt_pass.setText(password);
        }
        btn_login.setOnClickListener(v -> {
            if (edt_email.getText().toString().equals("") || edt_pass.getText().toString().equals("")){
                Toast.makeText(getContext(), "Không được bỏ trống", Toast.LENGTH_SHORT).show();
                return;
            }
            String email = edt_email.getText().toString();
            String pass = edt_pass.getText().toString();
            auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Home_Fragment()).commit();
                    } else {
                        Toast.makeText(getContext(), "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                    }
                    
                }
            });

        });
    }
}