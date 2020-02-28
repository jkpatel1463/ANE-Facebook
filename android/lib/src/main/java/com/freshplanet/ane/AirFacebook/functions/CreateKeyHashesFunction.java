package com.freshplanet.ane.AirFacebook.functions;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREObject;
import com.freshplanet.ane.AirFacebook.AirFacebookExtension;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CreateKeyHashesFunction extends BaseFunction {
    public FREObject call(FREContext context, FREObject[] args)
    {
        super.call(context, args);
        String packageName = getStringFromFREObject(args[0]);

        try {
            PackageInfo info = context.getActivity().getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String keyHash =Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.d("KeyHash:", keyHash);

                AirFacebookExtension.log("Facebook KeyHashes: " + keyHash);
                try {
                    FREObject result = FREObject.newObject(keyHash);
                    return result;
                } catch (Exception e) {
                    e.printStackTrace();
                    AirFacebookExtension.log("CreateKeyHashesFunction ERROR " + e.getMessage());
                    return null;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        return null;
    }
}
