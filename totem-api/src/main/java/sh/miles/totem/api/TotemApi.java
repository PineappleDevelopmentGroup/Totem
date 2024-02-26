package sh.miles.totem.api;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * The forward facing totem API
 */
public class TotemApi {

    private static Totem apiInstance;

    /**
     * Sets the API instance of totem
     *
     * @param apiInstance the api instance
     */
    @ApiStatus.Internal
    public static void setApiInstance(@NotNull final Totem apiInstance) {
        Preconditions.checkArgument(apiInstance != null, "The provided api instance must not be null");
        TotemApi.apiInstance = apiInstance;
    }

    /**
     * Gets the totem ApiInstance
     *
     * @return the api instance
     */
    @NotNull
    public static Totem getApiInstance() {
        return apiInstance;
    }
}
