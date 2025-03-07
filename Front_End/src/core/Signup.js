import React, { useState } from "react";
import { Navigate, Link as RouterLink } from "react-router-dom";
import { signup } from "../auth/authapi";
import {
  Container,
  Box,
  Typography,
  TextField,
  Button,
  Link,
  Paper,
  Avatar,
  Grid,
  Alert,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
} from "@mui/material";
import { PersonAddOutlined } from "@mui/icons-material";

const Signup = () => {
  const [values, setValues] = useState({
    firstName: "",
    lastName: "",
    email: "",
    password: "",
    role: "customer", // Default role
    error: "",
    success: false,
  });
  const [loading, setLoading] = useState(false);
  const { firstName, lastName, email, password, role, error, success } = values;

  const handleChange = (name) => (event) => {
    setValues({
      ...values,
      error: false,
      [name]: event.target.value,
    });
  };

  const onSubmit = (event) => {
    event.preventDefault();
    setValues({ ...values, error: false });

    signup({ firstName, lastName, email, password, role }) // Updated API payload
      .then((data) => {
        if (data.error) {
          setValues({ ...values, error: data.error, success: false });
        } else {
          setValues({
            ...values,
            firstName: "",
            lastName: "",
            email: "",
            password: "",
            role: "customer", // Reset to default role
            error: "",
            success: true,
          });
        }
      })
      .catch(() => console.log("Error in signup"));
  };

  return (
    <Container component="main" maxWidth="sm">
      <Paper
        elevation={3}
        sx={{
          marginTop: 8,
          p: 4,
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
        }}
      >
        
        <Typography component="h1" variant="h5">
          Sign up
        </Typography>

        {error && (
          <Alert severity="error" sx={{ width: "100%", mt: 2 }}>
            {error}
          </Alert>
        )}

        <Box component="form" onSubmit={onSubmit} noValidate sx={{ mt: 3 }}>
          <Grid container spacing={2}>
            <Grid item xs={12} sm={6}>
              <TextField
                name="firstName"
                required
                fullWidth
                id="firstName"
                label="First Name"
                autoFocus
                onChange={handleChange("firstName")}
                value={firstName}
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                name="lastName"
                required
                fullWidth
                id="lastName"
                label="Last Name"
                onChange={handleChange("lastName")}
                value={lastName}
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                required
                fullWidth
                id="email"
                label="Email Address"
                name="email"
                autoComplete="email"
                onChange={handleChange("email")}
                type="email"
                value={email}
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                required
                fullWidth
                name="password"
                label="Password"
                type="password"
                id="password"
                autoComplete="new-password"
                onChange={handleChange("password")}
                value={password}
              />
            </Grid>

            <Grid item xs={12}>
              <FormControl fullWidth>
                <InputLabel id="role-label">Role</InputLabel>
                <Select
                  labelId="role-label"
                  id="role"
                  name="role"
                  label="Role"
                  onChange={handleChange("role")}
                  value={role}
                >
                  <MenuItem value="customer">Customer</MenuItem>
                  <MenuItem value="manager">Event Manager</MenuItem>
                </Select>
              </FormControl>
            </Grid>
          </Grid>
          <Button
            type="submit"
            fullWidth
            variant="contained"
            sx={{ mt: 3, mb: 2 }}
            disabled={loading}
          >
            {loading ? "Signing up..." : "Sign Up"}
          </Button>
          <Grid container justifyContent="flex-end">
            <Grid item>
              <Link component={RouterLink} to="/signin" variant="body2">
                Already have an account? Sign in
              </Link>
            </Grid>
          </Grid>
        </Box>
      </Paper>
      {success && (
        <Alert severity="success" sx={{ mt: 3 }}>
          New account created successfully. Please{" "}
          <RouterLink to="/signin">Login Here</RouterLink>.
        </Alert>
      )}
    </Container>
  );
};

export default Signup;



// import React, { useState } from "react";
// import { Navigate, Link as RouterLink } from "react-router-dom";
// import { signup } from "../auth/authapi";
// import {
//     Container,
//     Box,
//     Typography,
//     TextField,
//     Button,
//     Link,
//     Paper,
//     Avatar,
//     Grid,
//     Alert,
//     FormControl,
//     InputLabel,
//     Select,
//     MenuItem,
//   } from "@mui/material"
//   import { PersonAddOutlined } from "@mui/icons-material"

// const Signup = () => {
//   const [values, setValues] = useState({
//     name: "",
//     email: "",
//     password: "",
//     role: "customer", // Default role
//     error: "",
//     success: false,
//   });
//   const [loading, setLoading] = useState(false)
//   const { name, email, password, role, error, success } = values;

//   const handleChange = (name) => (event) => {
//     setValues({
//       ...values,
//       error: false,
//       [name]: event.target.value,
//     });
//   };

//   const onSubmit = (event) => {
//     event.preventDefault();
//     setValues({ ...values, error: false });
//     signup({ name, email, password, role }) // Include role in API call
//       .then((data) => {
//         if (data.error) {
//           setValues({ ...values, error: data.error, success: false });
//         } else {
//           setValues({
//             ...values,
//             name: "",
//             email: "",
//             password: "",
//             role: "customer", // Reset to default role
//             error: "",
//             success: true,
//           });
//         }
//       })
//       .catch(() => console.log("Error in signup"));
//   };

//   const signUpForm = () => (
//     <>
//     {/* <Navbar /> */}
//       <Container component="main" maxWidth="sm">
//         <Paper
//           elevation={3}
//           sx={{
//             marginTop: 8,
//             p: 4,
//             display: "flex",
//             flexDirection: "column",
//             alignItems: "center",
//           }}
//         >
//           <Avatar sx={{ m: 1, bgcolor: "primary.main" }}>
//             <PersonAddOutlined />
//           </Avatar>
//           <Typography component="h1" variant="h5">
//             Sign up
//           </Typography>

//           {error && (
//             <Alert severity="error" sx={{ width: "100%", mt: 2 }}>
//               {error}
//             </Alert>
//           )}

//           <Box component="form" onSubmit={onSubmit} noValidate sx={{ mt: 3 }}>
//             <Grid container spacing={2}>
//               <Grid item xs={12} sm={6}>
//                 <TextField
//                   autoComplete="given-name"
//                   name="Name"
//                   required
//                   fullWidth
//                   id="Name"
//                   label="Name"
//                   autoFocus
//                   onChange={handleChange("name")}
//                         type="text"
//                              value={name}
//                 />
//               </Grid>
//               <Grid item xs={12}>
//                 <TextField
//                   required
//                   fullWidth
//                   id="email"
//                   label="Email Address"
//                   name="email"
//                   autoComplete="email"
//                   onChange={handleChange("email")}
//                              type="email"
//                            value={email}
//                 />
//               </Grid>
//               <Grid item xs={12}>
//                 <TextField
//                   required
//                   fullWidth
//                   name="password"
//                   label="Password"
//                   type="password"
//                   id="password"
//                   autoComplete="new-password"
//                   onChange={handleChange("password")}
//                   value={password}
//                 />
//               </Grid>
              
//               <Grid item xs={12}>
//                 <FormControl fullWidth>
//                   <InputLabel id="role-label">Role</InputLabel>
//                   <Select
//                     labelId="role-label"
//                     id="role"
//                     name="role"
//                     label="Role"
//                     onChange={handleChange("role")} 
//                     value={role}
//                   >
//                     <MenuItem value="customer">Customer</MenuItem>
//                     <MenuItem value="manager">Restaurant Manager</MenuItem>
//                   </Select>
//                 </FormControl>
//               </Grid>
//             </Grid>
//             <Button type="submit" fullWidth variant="contained" sx={{ mt: 3, mb: 2 }} disabled={loading}>
//               {loading ? "Signing up..." : "Sign Up"}
//             </Button>
//             <Grid container justifyContent="flex-end">
//               <Grid item>
//                 <Link component={RouterLink} to="/login" variant="body2">
//                   Already have an account? Sign in
//                 </Link>
//               </Grid>
//             </Grid>
//           </Box>
//         </Paper>
//       </Container>
//       </>
//     // <div className="row">
//     //   <div className="col-md-6 offset-sm-3 text-left">
//     //     <form>
//     //       <div className="form-group">
//     //         <label className="text-dark">Name</label>
//     //         <input
//     //           className="form-control"
//     //           placeholder="Enter your name"
//     //           onChange={handleChange("name")}
//     //           type="text"
//     //           value={name}
//     //         />
//     //       </div>

//     //       <div className="form-group">
//     //         <label className="text-dark">Email</label>
//     //         <input
//     //           className="form-control"
//     //           placeholder="Enter Email Address"
//     //           onChange={handleChange("email")}
//     //           type="email"
//     //           value={email}
//     //         />
//     //       </div>

//     //       <div className="form-group">
//     //         <label className="text-dark">Password</label>
//     //         <input
//     //           className="form-control"
//     //           placeholder="Enter Password"
//     //           onChange={handleChange("password")}
//     //           type="password"
//     //           value={password}
//     //         />
//     //       </div>

//     //       <div className="form-group">
//     //         <label className="text-dark">Role</label>
//     //         <select className="form-control" onChange={handleChange("role")} value={role}>
//     //           <option value="customer">Customer</option>
//     //           <option value="manager">Manager</option>
//     //           <option value="admin">Admin</option>
//     //         </select>
//     //       </div>

//     //       <div className="d-grid gap-2">
//     //         <button onClick={onSubmit} className="btn btn-success btn-block">
//     //           Submit
//     //         </button>
//     //       </div>
//     //     </form>
//     //   </div>
//     // </div>
//   );

//   return (
//     <>
//       {signUpForm()}
//       {success && (
//         <div className="alert alert-success">
//           New account was created successfully. Please <RouterLink to="/signin">Login Here</RouterLink>
//         </div>
//       )}
//       {error && <div className="alert alert-danger">{error}</div>}
//     </>
//   );
// };

// export default Signup;
