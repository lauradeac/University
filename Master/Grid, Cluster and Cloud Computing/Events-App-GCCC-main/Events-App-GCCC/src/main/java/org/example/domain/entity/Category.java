package org.example.domain.entity;

public enum Category {
    FESTIVAL("FESTIVAL"),
    CONCERT("CONCERT"),
    TRADE_SHOW("TRADE_SHOW"),
    WORKSHOP("WORKSHOP"),
    RETREAT("RETREAT"),
    CHARITY_EVENT("CHARITY_EVENT"),
    SPORTS_EVENT("SPORTS_EVENT");

    private String category;

    Category(String category) {
        this.category = category;
    }

    public String getDisplayName() {
        return category;
    }

    Category() {}

    public static Category fromString(String str) {
        for (Category category : Category.values()) {
            if (category.category.equalsIgnoreCase(str)) {
                return category;
            }
        }
        return null;
    }
}