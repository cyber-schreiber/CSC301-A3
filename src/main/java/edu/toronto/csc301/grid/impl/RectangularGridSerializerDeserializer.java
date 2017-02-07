package edu.toronto.csc301.grid.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import edu.toronto.csc301.grid.GridCell;
import edu.toronto.csc301.grid.IGrid;
import edu.toronto.csc301.grid.IGridSerializerDeserializer;
import edu.toronto.csc301.warehouse.Rack;

public class RectangularGridSerializerDeserializer implements IGridSerializerDeserializer {

	@Override
	public <T> void serialize(IGrid<T> grid, 
			OutputStream output, Function<T, byte[]> item2byteArray)
			throws IOException {
		
		List<GridCell> gc_list = new ArrayList<GridCell>();
		Iterator<GridCell> gridCells = grid.getGridCells();
		Map<GridCell, T> items = new HashMap<GridCell, T>();
		
		GridCell sw = gridCells.next();
		if (grid.getItem(sw) != null){
			items.put(sw, grid.getItem(sw));
		}
		
		gc_list.add(sw);
		
		int max_x = sw.x;
		int max_y = sw.y;
		int min_y = sw.y;
		int min_x = sw.x;
		while (gridCells.hasNext()){
			GridCell cell = gridCells.next();
			gc_list.add(cell);

			if (cell.x > max_x){
				max_x = cell.x;
			}
			
			if (cell.y > max_y){
				max_y = cell.y;
			}
			
			if (cell.y < min_y){
				min_y = cell.y;
			}
			
			if (cell.x < min_x){
				min_x = cell.x;
			}
			
			if (grid.getItem(cell) != null){
				items.put(cell, grid.getItem(cell));
			}
		}
		
		sw = GridCell.at(min_x, min_y);
		int width = 1 + max_x - sw.x;
		int height = 1 + max_y - sw.y;
		
		for (int x = sw.x; x < sw.x + width; x++){
			for (int y = sw.y; y < sw.y + height; y++){
				if (!gc_list.contains(GridCell.at(x, y))){
					throw new IllegalArgumentException();
				}
			}
		}
		
		
		StringBuilder s = new StringBuilder()
				.append(String.format("width: %d\n", width))
				.append(String.format("height: %d\n", height))
				.append(String.format("south-west: %d:%d\n", sw.x, sw.y));
		
		for (GridCell cell: items.keySet()){
			s.append(cell.x + ":" + cell.y + " " + "R:" + ((Rack) items.get(cell)).getCapacity());
			s.append("\n");
		}
		
		output.write(s.toString().getBytes());
	}

	@Override
	public <T> IGrid<T> deserialize(InputStream input, 
			Function<byte[], T> byteArray2item) throws IOException {
 		BufferedReader reader =  new BufferedReader(new InputStreamReader(input));
		int width = Integer.parseInt(reader.readLine().replaceAll("[^0-9]", ""));
		int height = Integer.parseInt(reader.readLine().replaceAll("[^0-9]", ""));
		String[] sw_coords = reader.readLine().split(" ")[1].split(":");
		GridCell sw = GridCell.at(Integer.parseInt(sw_coords[0]), 
				Integer.parseInt(sw_coords[1]));
		RectangularGrid<Rack> rg = new RectangularGrid<Rack>(width, height, sw);
		String line;
		while ((line = reader.readLine()) != null){
			String[] grid_coords = line.split(" ")[0].split(":");
			int rack_cap = Integer.parseInt(line.split(" ")[1].split(":")[1]);
			rg.addItem(GridCell.at(Integer.parseInt(grid_coords[0])
					, Integer.parseInt(grid_coords[1])), new Rack(rack_cap));
		}
		return (IGrid<T>) rg;
	}

}
