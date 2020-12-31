package cloud.lottify.detekt.rules

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider

class ImportRulesProvider : RuleSetProvider {

    override val ruleSetId: String = "imports"

    override fun instance(config: Config): RuleSet = RuleSet(
        ruleSetId,
        listOf(
            AdaptersImportInDomainPackage()
        )
    )
}
