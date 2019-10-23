package org.acra.util;

import android.content.Context;
import org.apache.http.conn.scheme.SocketFactory;

public final class DefaultHttpsSocketFactoryFactory implements HttpsSocketFactoryFactory {
    public static final HttpsSocketFactoryFactory INSTANCE = new DefaultHttpsSocketFactoryFactory();

    public SocketFactory create(Context context) {
        return new TlsSniSocketFactory();
    }
}
