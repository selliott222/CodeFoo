#Ruby Grid Challenge - Shane Elliott

v1 = ARGV[0]
v2 = ARGV[1]
if (v1 != nil) && (v2 != nil)
	$width = v1.to_i
	$height = v2.to_i
else
	$width = 3
	$height = 3
end

$area = $width * $height

class ChainEntry 
	attr_accessor :value, :index_row, :index_col
	
	def initialize(val, row, col)
		@value = val
		@index_row = row
		@index_col = col
	end
end

def randomVarible()
	return Random.rand(0...10);
end

def displayGrid(g)
	puts "\n//Code Foo Grid Challenge."
	puts "//Run script with parameters int width, int height for the grid"
	puts "\nGrid: #$width x #$height Width: #$width Area: #$area"
	for row in 0 ... g.size
		print g[row].to_s + "\n"
	end

	puts "\nValid Combinations: "
	
	time1 = Time.now
	
	for i in 0 ... $width
		for j in 0 ... $height
			findChain(g, i, j, [])
		end
	end
	
	time2 = Time.now
	
	puts "(Runtime: #{time2 - time1} seconds)"
end

def findChain(g, row, col, array)
	
	if (row < 0) || (row > g.size - 1) 
		return
	elsif (col < 0) || (col > g[0].size - 1) 
		return
	elsif (array.size > $area)
		return
	end
	
	chain_total = g[row][col]
	
	for index in 0 ... array.size
		chain_total += array[index].value 
		
		if (array[index].index_row == row && array[index].index_col == col) 
			return
		end
	end
	
	array.push(ChainEntry.new(g[row][col], row, col))
	
	if chain_total == $area #9 = area
		checkNewAnswerThenPrint(array)
		return
	elsif chain_total > $area #9 = area
		return
	elsif chain_total < $area #9 = area
			
		findChain(g, row-1, col-1, Marshal.load( Marshal.dump(array)))
		findChain(g, row-1, col, Marshal.load( Marshal.dump(array)))
		findChain(g, row-1, col+1, Marshal.load( Marshal.dump(array)))
		findChain(g, row, col-1, Marshal.load( Marshal.dump(array)))
		findChain(g, row, col+1, Marshal.load( Marshal.dump(array)))
		findChain(g, row+1, col-1, Marshal.load( Marshal.dump(array)))
		findChain(g, row+1, col, Marshal.load( Marshal.dump(array)))
		findChain(g, row+1, col+1, Marshal.load( Marshal.dump(array)))
	end
	
end

def checkNewAnswerThenPrint(a)
	if (a.size < ($width - 1))
		return
	end
	
	for i in 0 ... $valid_list.size
		if (sameArray(a, $valid_list[i]))
			return
		end
	end
	
	$valid_list.push(a)
	
	for index in 0 ... a.size
		print a[index].value.to_s
		if (index != a.size - 1)
			print " + "
		else
			print " = #$area"
		end
	end
	
	puts "\n"
end

def sameArray(a, b)
	if (a.size != b.size)
		return false
	end
	
	a = a.sort {|x, y| x.value <=> y.value}
	b = b.sort {|x, y| x.value <=> y.value}
	
	for k in 0...a.size
		if (a[k].value != b[k].value)
			return false
		end
	end
	
	return true
end

grid = Array.new($height)

for 
	r in 0 ... grid.size
	grid[r] = Array.new($width)
	
	for s in 0 ... grid[r].size
		grid[r][s] = randomVarible
	end
end

$valid_list = []

displayGrid(grid)

