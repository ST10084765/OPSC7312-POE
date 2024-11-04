package com.example.smarthealth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var callbackManager: CallbackManager
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    private lateinit var forgotPasswordButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialize UI elements
        emailEditText = findViewById(R.id.username)
        passwordEditText = findViewById(R.id.password)
        loginButton = findViewById(R.id.login_button)
        registerButton = findViewById(R.id.register_button)
        forgotPasswordButton = findViewById(R.id.forgot_password_button)

        // Set up Google Sign-In options
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Initialize Facebook SDK
        callbackManager = CallbackManager.Factory.create()
        val facebookLoginButton = findViewById<LoginButton>(R.id.facebook_login_button)
        facebookLoginButton.setPermissions("email", "public_profile")
        facebookLoginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d("LoginActivity", "Facebook token: ${loginResult.accessToken.token}")
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                Log.d("LoginActivity", "Facebook login cancelled")
            }

            override fun onError(error: FacebookException) {
                Log.e("LoginActivity", "Facebook login failed", error)
            }
        })

        // Login button click listener
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password)
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }

        // Register button click listener
        registerButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                registerUser(email, password)
            } else {
                Toast.makeText(this, "Please enter email and password for registration", Toast.LENGTH_SHORT).show()
            }
        }

        // Forgot password button click listener
        forgotPasswordButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            if (email.isNotEmpty()) {
                sendPasswordReset(email)
            } else {
                Toast.makeText(this, "Please enter your email to reset password", Toast.LENGTH_SHORT).show()
            }
        }

        // Google Sign-In button click listener
        findViewById<View>(R.id.google_sign_in_button).setOnClickListener {
            signInWithGoogle()
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("LoginActivity", "Login successful")
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                } else {
                    Log.w("LoginActivity", "Login failed", task.exception)
                    Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("LoginActivity", "Registration successful")
                    Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                } else {
                    Log.w("LoginActivity", "Registration failed", task.exception)
                    Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun sendPasswordReset(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("LoginActivity", "Password reset email sent")
                    Toast.makeText(this, "Password reset email sent!", Toast.LENGTH_SHORT).show()
                } else {
                    Log.w("LoginActivity", "Failed to send password reset email", task.exception)
                    Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Log.w("LoginActivity", "Google sign-in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("LoginActivity", "Google sign-in successful")
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                } else {
                    Log.w("LoginActivity", "Google sign-in failed", task.exception)
                }
            }
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = com.google.firebase.auth.FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("LoginActivity", "Facebook sign-in successful")
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                } else {
                    Log.w("LoginActivity", "Facebook sign-in failed", task.exception)
                }
            }
    }
}
