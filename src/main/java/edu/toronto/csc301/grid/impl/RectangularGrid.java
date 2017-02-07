package edu.toronto.csc301.grid.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import edu.toronto.csc301.grid.GridCell;
import edu.toronto.csc301.grid.IGrid;

public class RectangularGrid<T> implements IGrid<T> {
	private int width;
	private int height;
	private GridCell sw;
	private Set<GridCell> cells = new HashSet<GridCell>();
	private Map<String, T> items = new HashMap<String, T>();

	public RectangularGrid(int width, int height, GridCell sw){
		
		if (width <= 0 || height <= 0){
			throw new IllegalArgumentException();
		}
		
		if (sw == null){
			throw new NullPointerException();
		}
		
		this.width = width;
		this.height = height;
		this.sw = sw;
	}

	@Override
	public T getItem(GridCell cell) {
		
		if (cell.x > this.width + this.sw.x || cell.y > this.height + this.sw.y ){
			throw new IllegalArgumentException();
		}
		
		if (cell.x < this.sw.x || cell.y < this.sw.y){
			throw new IllegalArgumentException();
		}
		
		return this.items.get(cell.toString());
	}
	
	public void addItem(GridCell cell, T rack){
		this.items.put(cell.toString(), rack);
	}

	@Override
	public Iterator<GridCell> getGridCells() {
		for (int x = this.sw.x; x < this.sw.x + this.width; x++){
			for (int y = this.sw.y; y < this.sw.y + this.height; y++){
				this.cells.add(GridCell.at(x, y));
			}
		}
		return this.cells.iterator();
	}

	@Override
	public boolean hasCell(GridCell cell) {
		for (int x = this.sw.x; x < width + this.sw.x; x++){
			for (int y = this.sw.y; y < height + this.sw.y; y++){
				if (x == cell.x && y == cell.y){
					return true;
				}
			}
		}
		
		return false;
	}
	
	@Override
	public String toString(){
		return new StringBuilder()
		.append(String.format("width: %d\n", this.width))
		.append(String.format("height: %d\n", this.height))
		.append(String.format("south-west: %d:%d\n", this.sw.x, this.sw.y))
		.append(this.items.toString()).toString();
	}
}
