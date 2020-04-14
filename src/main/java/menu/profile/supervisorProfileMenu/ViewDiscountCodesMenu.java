package menu.profile.supervisorProfileMenu;

import menu.menuAbstract.Menu;

/**
 * @author Aryan Ahadinia
 * @since 0.0.1
 */

public class ViewDiscountCodesMenu extends Menu {
    public ViewDiscountCodesMenu(Menu parentMenu) {
        super("View Discount Codes Menu", parentMenu);

        Menu ViewDiscountCode = new Menu("View Discount Code", this) {
            @Override
            public void show() {
            }

            @Override
            public void execute() {
            }
        };
        menusIn.put("^view discount code (\\w+)$", ViewDiscountCode);
        menuForShow.add("View Discount Code");

        Menu EditDiscountCode = new Menu("Edit Discount Code", this) {
            @Override
            public void show() {
            }

            @Override
            public void execute() {
            }
        };
        menusIn.put("^edit discount code (\\w+)$", EditDiscountCode);
        menuForShow.add("Edit Discount Code");

        Menu RemoveDiscountCode = new Menu("Remove Discount Code", this) {
            @Override
            public void show() {
            }

            @Override
            public void execute() {
            }
        };
        menusIn.put("^remove discount code (\\w+)$", RemoveDiscountCode);
        menuForShow.add("Remove Discount Code");
    }
}
