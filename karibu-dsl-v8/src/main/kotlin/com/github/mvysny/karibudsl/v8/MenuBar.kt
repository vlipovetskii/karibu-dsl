package com.github.mvysny.karibudsl.v8

import com.vaadin.server.Resource
import com.vaadin.ui.HasComponents
import com.vaadin.ui.MenuBar

@VaadinDsl
public fun (@VaadinDsl HasComponents).menuBar(block: (@VaadinDsl MenuBar).()->Unit = {}): MenuBar =
        init(MenuBar(), block)
@VaadinDsl
public fun (@VaadinDsl MenuBar).item(caption: String, menuSelected: ((MenuBar.MenuItem)->Unit)? = null, block: (@VaadinDsl MenuBar.MenuItem).()->Unit = {}): MenuBar.MenuItem =
        item(caption, null, menuSelected, block)
@VaadinDsl
public fun (@VaadinDsl MenuBar).item(caption: String, icon: Resource? = null, menuSelected: ((MenuBar.MenuItem)->Unit)? = null, block: (@VaadinDsl MenuBar.MenuItem).()->Unit = {}): MenuBar.MenuItem =
        addItem(caption, icon, if (menuSelected == null) null else MenuBar.Command { menuSelected(it) }).apply { block() }
@VaadinDsl
public fun (@VaadinDsl MenuBar.MenuItem).item(caption: String, menuSelected: ((MenuBar.MenuItem)->Unit)? = null, block: (@VaadinDsl MenuBar.MenuItem).()->Unit = {}): MenuBar.MenuItem =
        item(caption, null, menuSelected, block)
@VaadinDsl
public fun (@VaadinDsl MenuBar.MenuItem).item(caption: String, icon: Resource? = null, menuSelected: ((MenuBar.MenuItem)->Unit)? = null, block: (@VaadinDsl MenuBar.MenuItem).()->Unit = {}): MenuBar.MenuItem =
        addItem(caption, icon, if (menuSelected == null) null else MenuBar.Command { menuSelected(it) }).apply { block() }

/**
 * A drop-down button emulated via a [MenuBar]. See [https://demo.vaadin.com/valo-theme/#!menubars] for demo and details.
 */
@VaadinDsl
public fun (@VaadinDsl HasComponents).dropDownButton(caption: String, splitButton: Boolean = false, block: (@VaadinDsl MenuBar.MenuItem).()->Unit = {}): MenuBar = init(MenuBar()) {
    var item: MenuBar.MenuItem = item(caption)
    if (splitButton) {
        item = item("")
    }
    item.block()
}
