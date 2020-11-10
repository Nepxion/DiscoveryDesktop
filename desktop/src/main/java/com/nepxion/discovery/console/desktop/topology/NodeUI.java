package com.nepxion.discovery.console.desktop.topology;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.awt.Point;

public class NodeUI {
    public static final String THEME_DIRECTORY = "theme_3/";

    public static final String SERVICE_GROUP_LARGE_IMAGE = "service_group_80.png";
    public static final String SERVICE_GROUP_MIDDLE_IMAGE = "service_group_64.png";
    public static final String SERVICE_GROUP_SMALL_IMAGE = "service_group_48.png";

    public static final String GATEWAY_GROUP_LARGE_IMAGE = "gateway_group_80.png";
    public static final String GATEWAY_GROUP_MIDDLE_IMAGE = "gateway_group_64.png";
    public static final String GATEWAY_GROUP_SMALL_IMAGE = "gateway_group_48.png";

    public static final String SERVICE_BLACK_LARGE_IMAGE = THEME_DIRECTORY + "service_black_64.png";
    public static final String SERVICE_BLACK_MIDDLE_IMAGE = THEME_DIRECTORY + "service_black_48.png";
    public static final String SERVICE_BLACK_SMALL_IMAGE = THEME_DIRECTORY + "service_black_32.png";

    public static final String SERVICE_YELLOW_LARGE_IMAGE = THEME_DIRECTORY + "service_yellow_64.png";
    public static final String SERVICE_YELLOW_MIDDLE_IMAGE = THEME_DIRECTORY + "service_yellow_48.png";
    public static final String SERVICE_YELLOW_SMALL_IMAGE = THEME_DIRECTORY + "service_yellow_32.png";

    public static final String SERVICE_GRAY_LARGE_IMAGE = THEME_DIRECTORY + "service_gray_64.png";
    public static final String SERVICE_GRAY_MIDDLE_IMAGE = THEME_DIRECTORY + "service_gray_48.png";
    public static final String SERVICE_GRAY_SMALL_IMAGE = THEME_DIRECTORY + "service_gray_32.png";

    public static final String SERVICE_BLUE_LARGE_IMAGE = THEME_DIRECTORY + "service_blue_64.png";
    public static final String SERVICE_BLUE_MIDDLE_IMAGE = THEME_DIRECTORY + "service_blue_48.png";
    public static final String SERVICE_BLUE_SMALL_IMAGE = THEME_DIRECTORY + "service_blue_32.png";

    public static final String SERVICE_GREEN_LARGE_IMAGE = THEME_DIRECTORY + "service_green_64.png";
    public static final String SERVICE_GREEN_MIDDLE_IMAGE = THEME_DIRECTORY + "service_green_48.png";
    public static final String SERVICE_GREEN_SMALL_IMAGE = THEME_DIRECTORY + "service_green_32.png";

    public static final String GATEWAY_BLACK_LARGE_IMAGE = THEME_DIRECTORY + "gateway_black_64.png";
    public static final String GATEWAY_BLACK_MIDDLE_IMAGE = THEME_DIRECTORY + "gateway_black_48.png";
    public static final String GATEWAY_BLACK_SMALL_IMAGE = THEME_DIRECTORY + "gateway_black_32.png";

    public static final String GATEWAY_YELLOW_LARGE_IMAGE = THEME_DIRECTORY + "gateway_yellow_64.png";
    public static final String GATEWAY_YELLOW_MIDDLE_IMAGE = THEME_DIRECTORY + "gateway_yellow_48.png";
    public static final String GATEWAY_YELLOW_SMALL_IMAGE = THEME_DIRECTORY + "gateway_yellow_32.png";

    public static final String GATEWAY_GRAY_LARGE_IMAGE = THEME_DIRECTORY + "gateway_gray_64.png";
    public static final String GATEWAY_GRAY_MIDDLE_IMAGE = THEME_DIRECTORY + "gateway_gray_48.png";
    public static final String GATEWAY_GRAY_SMALL_IMAGE = THEME_DIRECTORY + "gateway_gray_32.png";

    public static final String GATEWAY_BLUE_LARGE_IMAGE = THEME_DIRECTORY + "gateway_blue_64.png";
    public static final String GATEWAY_BLUE_MIDDLE_IMAGE = THEME_DIRECTORY + "gateway_blue_48.png";
    public static final String GATEWAY_BLUE_SMALL_IMAGE = THEME_DIRECTORY + "gateway_blue_32.png";

    public static final String GATEWAY_GREEN_LARGE_IMAGE = THEME_DIRECTORY + "gateway_green_64.png";
    public static final String GATEWAY_GREEN_MIDDLE_IMAGE = THEME_DIRECTORY + "gateway_green_48.png";
    public static final String GATEWAY_GREEN_SMALL_IMAGE = THEME_DIRECTORY + "gateway_green_32.png";

    private NodeImageType imageType;
    private NodeSizeType sizeType;
    private String image;
    private Point location;
    private boolean horizontalPile;

    public NodeUI(NodeImageType imageType, NodeSizeType sizeType, Point location) {
        initialize(imageType, sizeType);

        this.location = location;
    }

    public NodeUI(NodeImageType imageType, NodeSizeType sizeType, boolean horizontalPile) {
        initialize(imageType, sizeType);

        this.horizontalPile = horizontalPile;
    }

    private void initialize(NodeImageType imageType, NodeSizeType sizeType) {
        this.imageType = imageType;
        this.sizeType = sizeType;
        switch (imageType) {
            case SERVICE_GROUP:
                switch (sizeType) {
                    case LARGE:
                        image = SERVICE_GROUP_LARGE_IMAGE;
                        break;
                    case MIDDLE:
                        image = SERVICE_GROUP_MIDDLE_IMAGE;
                        break;
                    case SMALL:
                        image = SERVICE_GROUP_SMALL_IMAGE;
                        break;
                }
                break;
            case GATEWAY_GROUP:
                switch (sizeType) {
                    case LARGE:
                        image = GATEWAY_GROUP_LARGE_IMAGE;
                        break;
                    case MIDDLE:
                        image = GATEWAY_GROUP_MIDDLE_IMAGE;
                        break;
                    case SMALL:
                        image = GATEWAY_GROUP_SMALL_IMAGE;
                        break;
                }
                break;
            case SERVICE:
                switch (sizeType) {
                    case LARGE:
                        image = SERVICE_GREEN_LARGE_IMAGE;
                        break;
                    case MIDDLE:
                        image = SERVICE_GREEN_MIDDLE_IMAGE;
                        break;
                    case SMALL:
                        image = SERVICE_GREEN_SMALL_IMAGE;
                        break;
                }
                break;
            case SERVICE_BLACK:
                switch (sizeType) {
                    case LARGE:
                        image = SERVICE_BLACK_LARGE_IMAGE;
                        break;
                    case MIDDLE:
                        image = SERVICE_BLACK_MIDDLE_IMAGE;
                        break;
                    case SMALL:
                        image = SERVICE_BLACK_SMALL_IMAGE;
                        break;
                }
                break;
            case SERVICE_YELLOW:
                switch (sizeType) {
                    case LARGE:
                        image = SERVICE_YELLOW_LARGE_IMAGE;
                        break;
                    case MIDDLE:
                        image = SERVICE_YELLOW_MIDDLE_IMAGE;
                        break;
                    case SMALL:
                        image = SERVICE_YELLOW_SMALL_IMAGE;
                        break;
                }
                break;
            case SERVICE_GRAY:
                switch (sizeType) {
                    case LARGE:
                        image = SERVICE_GRAY_LARGE_IMAGE;
                        break;
                    case MIDDLE:
                        image = SERVICE_GRAY_MIDDLE_IMAGE;
                        break;
                    case SMALL:
                        image = SERVICE_GRAY_SMALL_IMAGE;
                        break;
                }
                break;
            case SERVICE_BLUE:
                switch (sizeType) {
                    case LARGE:
                        image = SERVICE_BLUE_LARGE_IMAGE;
                        break;
                    case MIDDLE:
                        image = SERVICE_BLUE_MIDDLE_IMAGE;
                        break;
                    case SMALL:
                        image = SERVICE_BLUE_SMALL_IMAGE;
                        break;
                }
                break;
            case SERVICE_GREEN:
                switch (sizeType) {
                    case LARGE:
                        image = SERVICE_GREEN_LARGE_IMAGE;
                        break;
                    case MIDDLE:
                        image = SERVICE_GREEN_MIDDLE_IMAGE;
                        break;
                    case SMALL:
                        image = SERVICE_GREEN_SMALL_IMAGE;
                        break;
                }
                break;
            case GATEWAY:
                switch (sizeType) {
                    case LARGE:
                        image = GATEWAY_GREEN_LARGE_IMAGE;
                        break;
                    case MIDDLE:
                        image = GATEWAY_GREEN_MIDDLE_IMAGE;
                        break;
                    case SMALL:
                        image = GATEWAY_GREEN_SMALL_IMAGE;
                        break;
                }
                break;
            case GATEWAY_BLACK:
                switch (sizeType) {
                    case LARGE:
                        image = GATEWAY_BLACK_LARGE_IMAGE;
                        break;
                    case MIDDLE:
                        image = GATEWAY_BLACK_MIDDLE_IMAGE;
                        break;
                    case SMALL:
                        image = GATEWAY_BLACK_SMALL_IMAGE;
                        break;
                }
                break;
            case GATEWAY_YELLOW:
                switch (sizeType) {
                    case LARGE:
                        image = GATEWAY_YELLOW_LARGE_IMAGE;
                        break;
                    case MIDDLE:
                        image = GATEWAY_YELLOW_MIDDLE_IMAGE;
                        break;
                    case SMALL:
                        image = GATEWAY_YELLOW_SMALL_IMAGE;
                        break;
                }
                break;
            case GATEWAY_GRAY:
                switch (sizeType) {
                    case LARGE:
                        image = GATEWAY_GRAY_LARGE_IMAGE;
                        break;
                    case MIDDLE:
                        image = GATEWAY_GRAY_MIDDLE_IMAGE;
                        break;
                    case SMALL:
                        image = GATEWAY_GRAY_SMALL_IMAGE;
                        break;
                }
                break;
            case GATEWAY_BLUE:
                switch (sizeType) {
                    case LARGE:
                        image = GATEWAY_BLUE_LARGE_IMAGE;
                        break;
                    case MIDDLE:
                        image = GATEWAY_BLUE_MIDDLE_IMAGE;
                        break;
                    case SMALL:
                        image = GATEWAY_BLUE_SMALL_IMAGE;
                        break;
                }
                break;
            case GATEWAY_GREEN:
                switch (sizeType) {
                    case LARGE:
                        image = GATEWAY_GREEN_LARGE_IMAGE;
                        break;
                    case MIDDLE:
                        image = GATEWAY_GREEN_MIDDLE_IMAGE;
                        break;
                    case SMALL:
                        image = GATEWAY_GREEN_SMALL_IMAGE;
                        break;
                }
                break;
        }
    }

    public NodeImageType getImageType() {
        return imageType;
    }

    public NodeSizeType getSizeType() {
        return sizeType;
    }

    public String getImage() {
        return image;
    }

    public Point getLocation() {
        return location;
    }

    public boolean isHorizontalPile() {
        return horizontalPile;
    }
}