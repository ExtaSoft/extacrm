package ru.extas.web;

public class DataSourceMapping{
	private String propName;
	private String caption;
	private boolean visible;
	private boolean inGrid;
	private boolean collapsed;
	
	
	public DataSourceMapping(String propName, String caption, boolean visible,
			boolean inGrid, boolean collapsed) {
		super();
		this.propName = propName;
		this.caption = caption;
		this.visible = visible;
		this.inGrid = inGrid;
		this.collapsed = collapsed;
	}
	/**
	 * @return the propName
	 */
	public final String getPropName() {
		return propName;
	}
	/**
	 * @param propName the propName to set
	 */
	public final void setPropName(String propName) {
		this.propName = propName;
	}
	/**
	 * @return the caption
	 */
	public final String getCaption() {
		return caption;
	}
	/**
	 * @param caption the caption to set
	 */
	public final void setCaption(String caption) {
		this.caption = caption;
	}
	/**
	 * @return the visible
	 */
	public final boolean isVisible() {
		return visible;
	}
	/**
	 * @param visible the visible to set
	 */
	public final void setVisible(boolean visible) {
		this.visible = visible;
	}
	/**
	 * @return the inGrid
	 */
	public final boolean isInGrid() {
		return inGrid;
	}
	/**
	 * @param inGrid the inGrid to set
	 */
	public final void setInGrid(boolean inGrid) {
		this.inGrid = inGrid;
	}
	/**
	 * @return the collapsed
	 */
	public final boolean isCollapsed() {
		return collapsed;
	}
	/**
	 * @param collapsed the collapsed to set
	 */
	public final void setCollapsed(boolean collapsed) {
		this.collapsed = collapsed;
	}
}