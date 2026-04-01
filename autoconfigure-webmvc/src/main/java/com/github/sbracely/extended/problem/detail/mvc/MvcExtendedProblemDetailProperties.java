package com.github.sbracely.extended.problem.detail.mvc;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Spring WebMVC Extended Problem Detail Configuration Properties Class.
 * <p>
 * This class is used to bind configuration properties with the prefix {@code extended.problem-detail}
 * from {@code application.yml} or {@code application.properties}, providing enable/disable control
 * for the extended problem detail feature.
 * </p>
 * <p>
 * Configuration properties:
 * </p>
 * <ul>
 *     <li>{@code enabled} - Whether to enable the extended problem detail feature, defaults to {@code true}</li>
 * </ul>
 * <p>
 * Usage example:
 * </p>
 * <pre>{@code
 * # application.yml
 * extended:
 *   problem-detail:
 *     enabled: true  # Enable extended problem detail handling
 * }</pre>
 * <pre>{@code
 * # application.properties
 * extended.problem-detail.enabled=true
 * }</pre>
 *
 * @see ConfigurationProperties
 * @see FluxExtendedProblemDetailProperties WebFlux version of configuration properties
 * @since 0.0.1-SNAPSHOT
 */
@ConfigurationProperties(prefix = "extended.problem-detail")
public class MvcExtendedProblemDetailProperties {

    /**
     * Extended problem detail feature enabled status.
     * <p>
     * When set to {@code true}, enables extended problem detail exception handling functionality;
     * when set to {@code false}, disables this feature and uses Spring's default error handling mechanism.
     * </p>
     */
    private boolean enabled = true;

    /**
     * Gets the enabled status of the extended problem detail feature.
     *
     * @return {@code true} if the feature is enabled, {@code false} if disabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets the enabled status of the extended problem detail feature.
     *
     * @param enabled {@code true} to enable the feature, {@code false} to disable it
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
