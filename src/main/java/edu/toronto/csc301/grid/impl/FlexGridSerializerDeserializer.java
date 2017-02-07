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

public class FlexGridSerializerDeserializer implements IGridSerializerDeserializer {

	@Override
	public <T> IGrid<T> deserialize(InputStream input, 
			Function<byte[], T> byteArray2item) throws IOException {
		
		List<GridCell> gridCells = new ArrayList<GridCell>();
		Map<GridCell, T> items = new HashMap<GridCell, T>();
		
		BufferedReader reader =  new BufferedReader(new InputStreamReader(input));
		String line;
		
		while ((line = reader.readLine()) != null){
			String[] coords = line.split(" ")[0].split(":");
			GridCell cell = GridCell.at(Integer.parseInt(coords[0]), 
					Integer.parseInt(coords[1]));
			gridCells.add(cell);
			
			if (line.split(" ").length > 1){
				items.put(cell, byteArray2item.apply(line.split(" ")[1].getBytes()));
			}
		}
		
		FlexGrid<T> fg =  new FlexGrid<T>(gridCells);
		for (GridCell cell: items.keySet()){
			fg.addItem(cell, items.get(cell));
		}
		
		return fg;
		
	}

	@Override
	public <T> void serialize(IGrid<T> grid, 
			OutputStream output, 
			Function<T, byte[]> item2byteArray)
			throws IOException {
		
		Iterator<GridCell> gridCells = grid.getGridCells();
		
		String s = "";
		while (gridCells.hasNext()){
			GridCell cell = gridCells.next();
			s = cell.x + ":" + cell.y;
			if (grid.getItem(cell) != null){
				s += " ";
				s += new String(item2byteArray.apply(grid.getItem(cell)));
				s += "\n";
			} else {
				s += "\n";
			}
			output.write(s.getBytes());
		}
	}

}
