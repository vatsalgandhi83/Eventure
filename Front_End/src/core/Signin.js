
import React, { useState } from "react";
import { Navigate} from "react-router-dom";
import { signin, authenticate, isAuthenticated } from "../auth/authapi";
import { 
    Container, Paper, Avatar, Typography, Alert, Box, 
    TextField, Button, Grid, Link, FormControlLabel, Checkbox
  } from "@mui/material";
  import { LockOutlined } from "@mui/icons-material";
  import { Link as RouterLink } from "react-router-dom"; 

const Signin = () => {
  const [values, setValues] = useState({
    email: "",
    password: "",
    error: "",
    loading: false,
    didRedirect: false,
  });

  const { email, password, error, loading, didRedirect } = values;
  const { user } = isAuthenticated();

  const handleChange = (name) => (event) => {
    setValues({
      ...values,
      error: false,
      [name]: event.target.value,
    });
  };

  const onSubmit = (event) => {
    event.preventDefault();
    setValues({ ...values, error: false, loading: true });
    signin({ email, password })
      .then((data) => {
        if (data.error) {
          setValues({ ...values, error: data.error, loading: false });
        } else {
          authenticate(data, () => {
            setValues({ ...values, didRedirect: true });
          });
        }
      })
      .catch(() => console.log("Signin request failed"));
  };

//   const performRedirect = () => {
//     if (didRedirect) {
//       if (user && user.role === "admin") {
//         return <Redirect to="/admin/dashboard" />;
//       } else if (user && user.role === "manager") {
//         return <Redirect to="/manager/dashboard" />;
//       } else {
//         return <Redirect to="/customer/dashboard" />;
//       }
//     }
//     if (isAuthenticated()) {
//       return <Redirect to="/" />;
//     }
//   };

const performRedirect = () => {
    const authData = isAuthenticated();
    if (!authData || !authData.user) {
        return null;
    }
    const { user } = authData;

    if (user.role === "manager") {
        return <Navigate to="/manager/dashboard" />;
    } else {
        return <Navigate to="/customer/dashboard" />;
    }
};
    

  

  return (
    <>
    <Grid container component="main" sx={{ height: "100vh" }}>
      <Grid
        item
        xs={false}
        sm={4}
        md={7}
        sx={{
        //   backgroundColor: "primary.main",
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          justifyContent: "center",
          color: "white",
          padding: 4,
          position: "relative",
        }}
      >
        {/* Decorative background pattern */}
        <Box
          sx={{
            position: "absolute",
            top: 0,
            left: 0,
            right: 0,
            bottom: 0,
            opacity: 0.1,
            backgroundImage: `url("data:image/svg+xml,%3Csvg width='60' height='60' viewBox='0 0 60 60' xmlns='http://www.w3.org/2000/svg'%3E%3Cg fill='none' fill-rule='evenodd'%3E%3Cg fill='%23ffffff' fill-opacity='1'%3E%3Cpath d='M36 34v-4h-2v4h-4v2h4v4h2v-4h4v-2h-4zm0-30V0h-2v4h-4v2h4v4h2V6h4V4h-4zM6 34v-4H4v4H0v2h4v4h2v-4h4v-2H6zM6 4V0H4v4H0v2h4v4h2V6h4V4H6z'/%3E%3C/g%3E%3C/g%3E%3C/svg%3E")`,
          }}
        />

        {/* Content */}
        <Box sx={{ position: "relative", textAlign: "center", maxWidth: "600px" }}>
          <Typography
            component="h1"
            variant="h2"
            sx={{
              mb: 4,
              fontWeight: "bold",
                background: "linear-gradient(45deg, #9162E4, #FF6B6B)",
                WebkitBackgroundClip: "text",
                color: "transparent",
              textShadow: "2px 2px 4px rgba(0,0,0,0.2)",
            //   background: "linear-gradient(45deg, #CADCFC, #00246B )", 
            }}
          >
            Eventure
          </Typography>
          
        </Box>
      </Grid>
      <Grid item xs={12} sm={8} md={5} component={Paper} elevation={6} square>
        <Box
          sx={{
            my: 8,
            mx: 4,
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
          }}
        >
          {/* <Avatar sx={{ m: 1, bgcolor: "secondary.main" }}>
            <LockOutlinedIcon />
          </Avatar> */}
          <Typography component="h1" variant="h5" sx={{color: "#5E35B1"}}>
            Sign in
          </Typography>
          <Box component="form" onSubmit={onSubmit} noValidate sx={{ mt: 1 }}>
            {error && (
              <Alert severity="error" sx={{ mb: 2 }}>
                {error}
              </Alert>
            )}
            <TextField
              margin="normal"
              required
              fullWidth
              id="email"
              label="Email Address"
              name="email"
              autoComplete="email"
              autoFocus
              value={email}
              onChange={handleChange("email")}
              sx={{
               "& .MuiOutlinedInput-root": {
          "& fieldset": {
            borderColor: "#CCCCCC", // Changed to gray for default state
          },
          "&:hover fieldset": {
            borderColor: "#9162E4", // Coral on hover
          },
          "&.Mui-focused fieldset": {
            borderColor: "#5E35B1", // Purple when focused
          },
        },
        "& .MuiInputLabel-root.Mui-focused": {
          color: "#5E35B1", // Purple label when focused
        },
      
                
              }}
            />
            <TextField
              margin="normal"
              required
              fullWidth
              name="password"
              label="Password"
              type="password"
              id="password"
              autoComplete="current-password"
              value={password}
              onChange={handleChange("password")}
              sx={{
                "& .MuiOutlinedInput-root": {
           "& fieldset": {
             borderColor: "#CCCCCC", // Changed to gray for default state
           },
           "&:hover fieldset": {
             borderColor: "#9162E4", // Coral on hover
           },
           "&.Mui-focused fieldset": {
             borderColor: "#5E35B1", // Purple when focused
           },
         },
         "& .MuiInputLabel-root.Mui-focused": {
           color: "#5E35B1", // Purple label when focused
         },
        }} 
            />
            <FormControlLabel control={<Checkbox value="remember" sx={{color: "#5E35B1"}}/>} label="Remember me" />
            <Button type="submit" fullWidth variant="contained" sx={{ mt: 3, mb: 2, background: "linear-gradient(45deg, #5E35B1, #9162E4)",
                  "&:hover": {
                    background: "linear-gradient(45deg, #3C1F8A, #7E54D1)",
                  }, }}>
              Sign In
            </Button>
            <Grid container>
              <Grid item>
                <Link href="/signup" variant="body2" sx={{color: "#5E35B1"}}>
                  {"Don't have an account? Sign Up"}
                </Link>
              </Grid>
            </Grid>
          </Box>
        </Box>
      </Grid>
    </Grid>


      {performRedirect()}
    </>
  );
};

export default Signin;
