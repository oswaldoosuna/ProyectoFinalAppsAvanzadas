package com.cystdi.proyectofinalappsavanzadas;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

/**
 * Clase Principal del Proyecto.
 * Manejador del Login a Facebook
 */
public class MainActivityFragment extends Fragment {

    //Objeto con las propiedades del login en facebook
    private AccessToken accessToken;
    private TextView textView;
    private ProfilePictureView profilePicture;

    //Manejador de las respuestas a las peticiones al botón del login
    private CallbackManager callbackManager;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());

        callbackManager= CallbackManager.Factory.create();
    }

    //Posibles resultados del proceso de login
    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        //Método cuando es existoso el login de facebook
        @Override
        public void onSuccess(LoginResult loginResult) {
            accessToken = loginResult.getAccessToken();
            //Objeto que contiene el perfil del usuario
            Profile profile = Profile.getCurrentProfile();
            if(profile != null){
                textView.setText("Bienvenido " + profile.getFirstName() + "!");
                profilePicture.setProfileId(profile.getId());
            }

        }
        //Método cuando no se dá el login
        @Override
        public void onCancel() {
            Toast.makeText(getActivity().getApplicationContext(), "No se aprobaron las credenciales...", Toast.LENGTH_SHORT).show();
        }

        //Método que se dispara al existir un error del login
        @Override
        public void onError(FacebookException error) {
            Toast.makeText(getActivity().getApplicationContext(), "Error de identificación...", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Se instancian los Objetos Gráficos del Botón y se asignan los permisos del usuario
        LoginButton loginButton = (LoginButton) view.findViewById(R.id.login_button);
        //loginButton.setReadPermissions("user_friend", "user_birthday");
        loginButton.setReadPermissions("user_friend");
        loginButton.setFragment(this);

        loginButton.registerCallback(callbackManager, callback);

        textView = (TextView) view.findViewById(R.id.text_details);
        profilePicture = (ProfilePictureView) view.findViewById(R.id.profilePicture);

        //Al desconectarse se reestablecen los valores por defaul en la parte gráfica
        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if(currentAccessToken == null){
                    textView.setText("Sesión no iniciada");
                    profilePicture.setProfileId("");
                }
            }
        };
    }
    //Repuesta a lallamda del Login de facebook
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
