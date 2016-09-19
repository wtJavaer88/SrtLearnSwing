package common.utils;

import java.util.UUID;

public final class UUIDUtil
{

    /**
     * @return
     */
    public final static String getUUID()
    {
        return UUID.randomUUID().toString();
    }
}
