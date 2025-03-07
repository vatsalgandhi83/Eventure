"use client"

import { useState } from "react"
import { useNavigate } from "react-router-dom" // Import for navigation
import {
  AppBar,
  Box,
  Toolbar,
  IconButton,
  Typography,
  Menu,
  Container,
  Avatar,
  MenuItem,
  Tooltip,
  useTheme,
  useMediaQuery,
} from "@mui/material"
import AccountCircleIcon from "@mui/icons-material/AccountCircle"
import PersonIcon from "@mui/icons-material/Person"
import EventIcon from "@mui/icons-material/Event"
import LogoutIcon from "@mui/icons-material/Logout"
import { signout} from "../auth/authapi";

function Navbar() {
  const [anchorElUser, setAnchorElUser] = useState(null)
  const theme = useTheme()
  const isMobile = useMediaQuery(theme.breakpoints.down("sm"))
  const navigate = useNavigate() // Initialize navigate for redirection

  const handleOpenUserMenu = (event) => {
    setAnchorElUser(event.currentTarget)
  }

  const handleCloseUserMenu = () => {
    setAnchorElUser(null)
  }

  // Signout function
//   const signout = (next) => {
//     if (typeof window !== "undefined") {
//       localStorage.removeItem("jwt")
//       next()
//       console.log("Signout successfully")
//     }
//   }

  const handleMenuItemClick = (action) => {
    // console.log(`${action} clicked`)
    handleCloseUserMenu()

    if (action === "Logout") {
      signout(() => {
        navigate("/signin")
      })
    } else if (action === "Profile") {
      
    } else if (action === "My Events") {
    
    }
  }

  // User menu items with icons
  const userMenuItems = [
    { text: "Profile", icon: <PersonIcon fontSize="small" sx={{ mr: 1 }} />, action: "Profile" },
    { text: "My Events", icon: <EventIcon fontSize="small" sx={{ mr: 1 }} />, action: "My Events" },
    { text: "Logout", icon: <LogoutIcon fontSize="small" sx={{ mr: 1 }} />, action: "Logout" },
  ]

  return (
    <AppBar position="static">
      <Container maxWidth="xl">
        <Toolbar disableGutters sx={{ justifyContent: "space-between" }}>
          {/* Logo */}
          <Typography
            variant="h6"
            component="a"
            href="/"
            sx={{
              fontFamily: "monospace",
              fontWeight: 700,
              letterSpacing: ".3rem",
              color: "inherit",
              textDecoration: "none",
            }}
          >
            Eventure
          </Typography>

          {/* Avatar with dropdown */}
          <Box sx={{ flexGrow: 0 }}>
            <Tooltip >
              <IconButton onClick={handleOpenUserMenu} sx={{ p: 0 }}>
                <Avatar/>
                  {/* <AccountCircleIcon /> */}
                {/* </Avatar> */}
              </IconButton>
            </Tooltip>
            <Menu
              sx={{ mt: "45px" }}
              id="menu-appbar"
              anchorEl={anchorElUser}
              anchorOrigin={{
                vertical: "top",
                horizontal: "right",
              }}
              keepMounted
              transformOrigin={{
                vertical: "top",
                horizontal: "right",
              }}
              open={Boolean(anchorElUser)}
              onClose={handleCloseUserMenu}
            >
              {userMenuItems.map((item) => (
                <MenuItem key={item.text} onClick={() => handleMenuItemClick(item.action)} sx={{ minWidth: 150 }}>
                  {item.icon}
                  <Typography textAlign="center">{item.text}</Typography>
                </MenuItem>
              ))}
            </Menu>
          </Box>
        </Toolbar>
      </Container>
    </AppBar>
  )
}

export default Navbar

