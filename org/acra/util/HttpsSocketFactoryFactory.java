package org.acra.util;

import android.content.Context;
import org.apache.http.conn.scheme.SocketFactory;

public interface HttpsSocketFactoryFactory {
    SocketFactory create(Context context);
}
