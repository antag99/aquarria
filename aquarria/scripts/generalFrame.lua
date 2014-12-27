-- Provides the name of the frame based on adjacent merges.
-- Intended for use in other scripts.
local function getFrame(mergeTop, mergeRight, mergeBottom, mergeLeft)
  if not mergeTop then
    --  -
    -- ? ?
    --  ?
    if not mergeRight then
      --  -
      -- ? -
      --  ?
      if not mergeBottom then
        --  -
        -- ? -
        --  -
        if not mergeLeft then
          --  -
          -- - -
          --  -
          return "no_merge"
        else
          --  -
          -- X -
          --  -
          return "left_merge"
        end
      else
        --  -
        -- ? -
        --  X
        if not mergeLeft then
          --  -
          -- - -
          --  X
          return "bottom_merge"
        else
          --  -
          -- X -
          --  X
          return "bottom_left_merge"
        end
      end
    else
      --  -
      -- ? X
      --  ?
      if not mergeBottom then
        --  -
        -- ? X
        --  -
        if not mergeLeft then
          --  -
          -- - X
          --  -
          return "right_merge"
        else
          --  -
          -- X X
          --  -
          return "left_right_merge"
        end
      else
        --  -
        -- ? X
        --  X
        if not mergeLeft then
          --  -
          -- - X
          --  X
          return "bottom_right_merge"
        else
          --  -
          -- X X
          --  X
          return "without_top_merge"
        end
      end
    end
  else
    --  X
    -- ? ?
    --  ?
    if not mergeRight then
      --  X
      -- ? -
      --  ?
      if not mergeBottom then
        --  X
        -- ? -
        --  -
        if not mergeLeft then
          --  X
          -- - -
          --  -
          return "top_merge"
        else
          --  X
          -- X -
          --  -
          return "top_left_merge"
        end
      else
        --  X
        -- ? -
        --  X
        if not mergeLeft then
          --  X
          -- - -
          --  X
          return "top_bottom_merge"
        else
          --  X
          -- X -
          --  X
          return "without_right_merge"
        end
      end
    else
      --  X
      -- ? X
      --  ?
      if not mergeBottom then
        --  X
        -- ? X
        --  -
        if not mergeLeft then
          --  X
          -- - X
          --  -
          return "top_right_merge"
        else
          --  X
          -- X X
          --  -
          return "without_bottom_merge"
        end
      else
        --  X
        -- ? X
        --  X
        if not mergeLeft then
          --  X
          -- - X
          --  X
          return "without_left_merge"
        else
          --  X
          -- X X
          --  X
          return "all_merge"
        end
      end
    end
  end
end

return getFrame
