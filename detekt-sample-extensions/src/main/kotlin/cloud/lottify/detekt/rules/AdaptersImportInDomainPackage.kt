package cloud.lottify.detekt.rules

import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.psi.KtImportDirective

class AdaptersImportInDomainPackage : Rule() {
    private val KtImportDirective.import: String?
        get() = importPath?.pathStr

    override val issue = Issue(
        javaClass.simpleName,
        Severity.CodeSmell,
        "Import from '$ADAPTERS_PACKAGE_PATH' package in the '$DOMAIN_LAYER_PACKAGE' package. " +
                "This violates dependency rule.",
        Debt.FIVE_MINS
    )

    override fun visitImportDirective(importDirective: KtImportDirective) {
        if (shouldReport(importDirective)) {
            report(
                CodeSmell(
                    issue = issue,
                    entity = Entity.from(importDirective),
                    message = "Importing '${importDirective.import}' which is not allowed in a domain layer."
                )
            )
        }
    }

    private fun shouldReport(importDirective: KtImportDirective): Boolean {
        return importDirective.isInDomainLayer() && importDirective.shouldReportAdaptersDependency()
    }

    private fun KtImportDirective.isInDomainLayer(): Boolean {
        return this.containingKtFile
            .packageDirective
            ?.qualifiedName
            ?.contains(DOMAIN_LAYER_PACKAGE) == true
    }

    private fun KtImportDirective.shouldReportAdaptersDependency(): Boolean {
        return this.import
            ?.contains(ADAPTERS_PACKAGE_PATH) == true
    }

    companion object {
        const val ADAPTERS_PACKAGE_PATH = "adapters"
        const val DOMAIN_LAYER_PACKAGE = "domain"
    }
}
