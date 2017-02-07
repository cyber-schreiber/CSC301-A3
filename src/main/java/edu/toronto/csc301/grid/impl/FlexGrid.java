package edu.toronto.csc301.grid.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.toronto.csc301.grid.GridCell;
import edu.toronto.csc301.grid.IGrid;
	
public class FlexGrid<T> implements IGrid<T> {

	private List<GridCell> cells = new ArrayList<GridCell>();
	private Map<String, T> items = new HashMap<String, T>();

	public FlexGrid(){};
	
	public FlexGrid(List<GridCell> cells){
		this.cells = cells;
	}
	
	@Override
	public T getItem(GridCell cell) {
		return this.items.get(cell.toString());
	}
	
	public void addItem(GridCell cell, T rack){
		this.items.put(cell.toString(), rack);
	}

	@Override
	public Iterator<GridCell> getGridCells() {
		return this.cells.iterator();
	}

	@Override
	public boolean hasCell(GridCell cell) {
		for (GridCell c: this.cells){
			if (c.x == cell.x && c.y == cell.y){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString(){
		return new StringBuilder()
		.append(this.cells.toString())
		.append(this.items.toString())
		.toString();
	}
}