package misk.config

import com.google.common.truth.Truth.assertThat
import misk.environment.Environment
import misk.environment.Environment.TESTING
import misk.environment.EnvironmentModule
import misk.testing.MiskTestRule
import misk.web.WebConfig
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

class ConfigTest {
  @get:Rule
  val miskTestRule = MiskTestRule(
      ConfigModule.create<TestConfig>("test_app"),
      EnvironmentModule(TESTING)
  )

  @Inject lateinit var test_config: TestConfig

  @field:[Inject Named("consumer_a")] lateinit var consumer_a_config: ConsumerConfig

  @field:[Inject Named("consumer_b")] lateinit var consumer_b_config: ConsumerConfig

  @Test
  fun testConfigIsProperlyParsed() {
    assertThat(test_config.web_config).isEqualTo(WebConfig(5678, 30_000))
    assertThat(test_config.consumer_a).isEqualTo(ConsumerConfig(0, 1))
    assertThat(test_config.consumer_b).isEqualTo(ConsumerConfig(1, 2))
  }

  @Test
  fun subConfigsAreNamedProperly() {
    assertThat(consumer_a_config).isEqualTo(ConsumerConfig(0, 1))
    assertThat(consumer_b_config).isEqualTo(ConsumerConfig(1, 2))
  }

  @Test
  fun environmentConfigOverridesCommon() {
    assertThat(test_config.web_config.port).isEqualTo(5678)
  }

  @Test
  fun defaultValuesAreUsed() {
    assertThat(test_config.consumer_a.min_items).isEqualTo(0)
  }
}