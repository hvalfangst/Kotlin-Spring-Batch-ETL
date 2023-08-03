package hvalfangst.etl.config

import jakarta.annotation.PostConstruct
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.Location
import org.flywaydb.core.api.configuration.ClassicConfiguration
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

/**
 * Configuration class for Flyway database migrations.
 *
 * This class sets up and performs database migrations using Flyway for both the source and target databases.
 * The migration paths for both databases are specified through properties, and Flyway is initialized and
 * invoked during application startup.
 *
 * @property sourceDatabase The data source for the source database to perform migrations.
 * @property targetDatabase The data source for the target database to perform migrations.
 * @property migrationPathSource The migration path for the source database specified through properties.
 * @property migrationPathTarget The migration path for the target database specified through properties.
 */
@Configuration
class FlywayConfig {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    private val sourceDatabase: DataSource? = null

    @Autowired
    private val targetDatabase: DataSource? = null

    @Value("\${env.sourceDatabase.migrationPath}")
    private val migrationPathSource: String = "null"

    @Value("\${env.targetDatabase.migrationPath}")
    private val migrationPathTarget: String = "null"

    /**
     * Performs Flyway database migrations on both the source and target databases during application startup.
     */
    @PostConstruct
    fun migrateFlyway() {
        log.info("[MIGRATION START] - - - SOURCE DATABASE - - - [MIGRATION START]")
        var configuration = ClassicConfiguration()
        configuration.dataSource = sourceDatabase
        configuration.setLocations(Location(migrationPathSource))
        var flyway = Flyway(configuration)
        flyway.migrate()
        log.info("[MIGRATION END] - - - SOURCE DATABASE - - - [MIGRATION END]")

        log.info("[MIGRATION START] - - - TARGET DATABASE - - - [MIGRATION START]")
        configuration = ClassicConfiguration()
        configuration.dataSource = targetDatabase
        configuration.setLocations(Location(migrationPathTarget))
        flyway = Flyway(configuration)
        flyway.migrate()
        log.info("[MIGRATION END] - - - TARGET DATABASE - - - [MIGRATION END]")
    }
}