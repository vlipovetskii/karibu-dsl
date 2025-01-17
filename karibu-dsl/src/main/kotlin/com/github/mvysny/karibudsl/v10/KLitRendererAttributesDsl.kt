package com.github.mvysny.karibudsl.v10

import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.shared.ThemeVariant
import kotlinx.css.CssBuilder
import kotlinx.css.hyphenize

/**
 * [KLitRendererAttribute] allows you to insert attribute of a html tag
 * [name] - the name of attribute
 * [value] - the value of attribute
 * [toString] - a html representation of the attribute
 * Example of usage:
 * ```kotlin
 * horizontalLayout(theme(spacing)) {
 *   ...
 * }
 * ```
 * Generated html code:
 * ```
 * <vaadin-horizontal-layout theme="spacing"></vaadin-horizontal-layout>
 * ```
 */
public data class KLitRendererAttribute(
    val name: String,
    val value: String
) {
    override fun toString(): String = "$name=\"$value\""
}

/**
 * [buildInlineStyleText] builds text value representation of the [style] attribute.
 * Example of usage:
 * ```
 * verticalLayout(style { lineHeight = +KLumoLineHeight.XS; }) {
 *   ...
 * }
 * ```
 * Generated html code:
 * ```
 * <vaadin-vertical-layout style="line-height: var(--lumo-line-height-xs)"></vaadin-vertical-layout>
 * ```
 */
private fun CssBuilder.buildInlineStyleText() =
    declarations.asSequence().joinToString(separator = "; ") { (key, value) ->
        "${key.hyphenize()}: $value"
    }

@VaadinDsl
public fun <TSource> KLitRendererTagsBuilder<TSource>.style(value: String) =
    KLitRendererAttribute("style", value)

@VaadinDsl
public fun <TSource> KLitRendererTagsBuilder<TSource>.style(block: CssBuilder.() -> Unit) =
    KLitRendererAttribute(
        "style",
        CssBuilder().apply(block).buildInlineStyleText()
    )

@VaadinDsl
public fun <TSource> KLitRendererTagsBuilder<TSource>.theme(vararg names: KLitRendererTheme) =
    KLitRendererAttribute(
        "theme",
        names.joinToString(separator = " ") { it.name })

@VaadinDsl
public fun <TSource> KLitRendererTagsBuilder<TSource>.themeVariant(vararg names: ThemeVariant) =
    KLitRendererAttribute(
        "theme",
        names.joinToString(separator = " ") { it.variantName })

/**
 * A `class` attribute added to the parent element.
 */
@VaadinDsl
public fun <TSource> KLitRendererTagsBuilder<TSource>.cssClass(vararg names: String) =
    KLitRendererAttribute("class", names.joinToString(separator = " "))

@VaadinDsl
public fun <TSource> KLitRendererTagsBuilder<TSource>.id(value: String) =
    KLitRendererAttribute("id", value)

@VaadinDsl
public fun <TSource> KLitRendererTagsBuilder<TSource>.name(value: KLitRendererBuilder.Property<TSource>) =
    KLitRendererAttribute("name", value.litItem)

@VaadinDsl
public fun <TSource> KLitRendererTagsBuilder<TSource>.src(value: KLitRendererBuilder.Property<TSource>) =
    KLitRendererAttribute("src", value.litItem)

/**
 * <vaadin-icon icon="vaadin:edit" slot="prefix"></vaadin-icon>
 *
 * Example of use:
 * ```kotlin
 * val columnIcon by property { row ->
 *                         if (row.isNew)
 *                             VaadinIcon.PLUS_CIRCLE.createIcon().icon
 *                         else
 *                             VaadinIcon.EDIT.createIcon().icon
 *                     }
 *  ...
 *  icon(+columnIcon)
 *  ...
 * ```
 */
@VaadinDsl
public fun <TSource> KLitRendererTagsBuilder<TSource>.icon(value: KLitRendererBuilder.Property<TSource>) =
    KLitRendererAttribute("icon", value.litItem)

@VaadinDsl
public fun <TSource> KLitRendererTagsBuilder<TSource>.icon(value: Icon) =
    KLitRendererAttribute("icon", value.icon)

@VaadinDsl
public fun <TSource> KLitRendererTagsBuilder<TSource>.img(value: KLitRendererBuilder.Property<TSource>) =
    KLitRendererAttribute("img", value.litItem)

@VaadinDsl
public fun <TSource> KLitRendererTagsBuilder<TSource>.href(value: String) =
    KLitRendererAttribute("href", value)

@VaadinDsl
public fun <TSource> KLitRendererTagsBuilder<TSource>.href(value: KLitRendererBuilder.Property<TSource>) =
    KLitRendererAttribute("href", value.litItem)

@VaadinDsl
public fun <TSource> KLitRendererTagsBuilder<TSource>.click(value: KLitRendererBuilder.Function<TSource>) =
    KLitRendererAttribute("@click", value.litItem)
