package com.jones.vform.activity

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.jones.vform.data.AppController
import com.jones.vform.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    private var oneTapClient: SignInClient? = null
    private var signInRequest: BeginSignInRequest? = null

    private lateinit var binding: ActivitySignInBinding

    private val oneTapResult =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
            try {
                if (it.resultCode == Activity.RESULT_OK) {
                    try {
                        val credentials = oneTapClient?.getSignInCredentialFromIntent(it.data)
                        val idToken = credentials?.googleIdToken
                        firebaseAuthWithGoogle(idToken!!)
                    } catch (e: ApiException) {
                        Log.w("Sign", "Google sign in failed", e)
                        Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.w("Sign", "Google sign in failed with status code: ${it.resultCode}")
                    Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show()
                }
            } catch (e: ApiException) {
                Log.w("Sign", "Google sign in failed", e)
                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show()
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        googleSign()
        clickListener()
    }

    private fun clickListener() {
        binding.signInBt.setOnClickListener {
            displaySignIn()
        }
    }

    private fun googleSign() {

        oneTapClient = Identity.getSignInClient(this)

        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId("72441042418-ruakq2kkt08pnd8ndafmqha9jvjlggpp.apps.googleusercontent.com")
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .setAutoSelectEnabled(false)
            .build()

    }

    private fun displaySignIn() = oneTapClient?.beginSignIn(signInRequest!!)
        ?.addOnSuccessListener { performAuthentication(it) }
        ?.addOnFailureListener { e -> Log.d("Authentication", "Error: $e") }


    private fun performAuthentication(result: BeginSignInResult) {
        try {
            oneTapResult.launch(
                IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
            )
        } catch (ex: IntentSender.SendIntentException) {

        }
    }


    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    //Login Success
                    //TODO: Point details yet to be added and direct it to next page
                    Toast.makeText(this, "Login successful", Toast.LENGTH_LONG).show()
                    goToNextActivity()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("CreateProfileActivity", "signInWithCredential:failure", task.exception)
                }
            }
    }

    private fun goToNextActivity() {
        if (FirebaseAuth.getInstance().currentUser != null) {
            AppController.instance.user = FirebaseAuth.getInstance().currentUser
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        goToNextActivity()
    }
}