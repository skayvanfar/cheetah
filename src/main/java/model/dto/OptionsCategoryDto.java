/*
 * Cheetah - A Free Fast Downloader
 *
 * Copyright © 2017 Saeed Kayvanfar
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package model.dto;

import java.awt.*;
import java.util.List;

/**
 * @author <a href="kayvanfar.sj@gmail.com">Saeed Kayvanfar</a> on 6/8/17.
 */
public class OptionsCategoryDto {

    private String categoryName = "Category";
    private List<String> nodeNames;
    private Color color = new Color(238, 238, 244);

    public OptionsCategoryDto(String categoryName, List<String> nodeNames, Color color) {
        this.categoryName = categoryName;
        this.nodeNames = nodeNames;
        this.color = color;
    }

    public OptionsCategoryDto(String categoryName, List<String> nodeNames) {
        this.categoryName = categoryName;
        this.nodeNames = nodeNames;
    }

    public OptionsCategoryDto(List<String> nodeNames) {
        this.nodeNames = nodeNames;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<String> getNodeNames() {
        return nodeNames;
    }

    public void setNodeNames(List<String> nodeNames) {
        this.nodeNames = nodeNames;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setColor(int r, int g, int b) {
        this.color = new Color(r, g, b);
    }

    @Override
    public String toString() {
        return "OptionsCategoryDto{" +
                "categoryName='" + categoryName + '\'' +
                ", nodeNames=" + nodeNames +
                ", color=" + color +
                '}';
    }
}
