export interface User {
  user_id: number,
  email: string,
  name: string,
  image: string,
  is_admin: boolean,
  
  token: string,
}