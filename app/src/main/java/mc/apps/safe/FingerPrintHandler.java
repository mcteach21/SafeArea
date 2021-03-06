package mc.apps.safe;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

public class FingerPrintHandler extends FingerprintManager.AuthenticationCallback {

    private CancellationSignal cancellationSignal;
    private Context appContext;

    McFPManager.IAuthentify _notifier;
    public FingerPrintHandler(Context context, McFPManager.IAuthentify notifier) {
        appContext = context;
        _notifier = notifier;
    }

    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
        cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(appContext,
                Manifest.permission.USE_FINGERPRINT) !=
                PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        Toast.makeText(appContext,  "Erreur Authentification\n" + errString, Toast.LENGTH_LONG).show();
    }
    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        Toast.makeText(appContext,  "Aide Authentication\n" + helpString, Toast.LENGTH_LONG).show();
    }
    @Override
    public void onAuthenticationFailed() {
        Toast.makeText(appContext,"Echec Authentication!", Toast.LENGTH_LONG).show();
        _notifier.notify(McFPManager.IAuthentify.AUTH_RESULT.FAILED);
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        Toast.makeText(appContext,"Authentication Réussie!", Toast.LENGTH_LONG).show();
        _notifier.notify(McFPManager.IAuthentify.AUTH_RESULT.OK);
    }
}