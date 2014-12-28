
local function treeFrame(world, x, y)
  local trunkTop = world:inBounds(x, y + 1) and world:getTileType(x, y + 1) == TileType.tree and
    world:isAttached(x, y + 1, Direction.SOUTH)
  local trunkBottom = world:inBounds(x, y - 1) and world:getTileType(x, y - 1) == TileType.tree and
    world:isAttached(x, y, Direction.SOUTH)
  local treeLeft = world:inBounds(x - 1, y) and world:getTileType(x - 1, y) == TileType.tree and
    world:isAttached(x - 1, y, Direction.EAST)
  local treeRight = world:inBounds(x + 1, y) and world:getTileType(x + 1, y) == TileType.tree and
    world:isAttached(x + 1, y, Direction.WEST)
  
  if trunkTop and trunkBottom then
    if treeLeft and treeRight then return "middle_trunk_with_both_branches" end
    if treeLeft then return "middle_trunk_with_left_branch" end
    if treeRight then return "middle_trunk_with_right_branch" end
    return "middle_trunk"
  end
  
  if not trunkTop and trunkBottom then
    if treeLeft and treeRight then return "top_trunk_with_both_branches" end
    if treeLeft then return "top_trunk_with_left_branch" end
    if treeRight then return "top_trunk_with_right_branch" end
    return "top_trunk_leaves"
  end
  
  if not trunkBottom and trunkTop then
    if treeLeft and treeRight then return "bottom_trunk_with_both_stubs" end
    if treeLeft then return "bottom_trunk_with_left_stub" end
    if treeRight then return "bottom_trunk_with_right_stub" end
    return "bottom_trunk"
  end
  
  local trunkLeft = world:inBounds(x - 1, y) and world:getTileType(x - 1, y) == TileType.tree and
    world:isAttached(x, y, Direction.WEST)
  local trunkRight = world:inBounds(x + 1, y) and world:getTileType(x + 1, y) == TileType.tree and
    world:isAttached(x, y, Direction.EAST)
  
  if not trunkBottom and not trunkTop then
    if not world:isAttached(x, y, Direction.SOUTH) then
      if trunkRight then return "left_branch_leaves" end
      if trunkLeft then return "right_branch_leaves" end
      return "stub" -- Invalid frame; add special texture?
    else
      if trunkRight then return "left_stub" end
      if trunkLeft then return "right_stub" end
      if treeLeft and treeRight then return "stub_with_both_stubs" end
      if treeLeft then return "stub_with_left_stub" end
      if treeRight then return "stub_with_right_stub" end
      return "stub"
    end
  end
  return "stub" -- Invalid frame; add special texture?
end

return treeFrame
